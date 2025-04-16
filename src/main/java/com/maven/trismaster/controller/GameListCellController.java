package com.maven.trismaster.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import com.maven.trismaster.App;
import com.maven.trismaster.connection.HttpConnection;
import com.maven.trismaster.entity.Match;
import com.maven.trismaster.entity.Stat;
import com.maven.trismaster.entity.Step;
import com.maven.trismaster.entity.User;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

public class GameListCellController extends ListCell<Match> implements Initializable, Job {
	@FXML Pane tag, notice;
	@FXML Label player_1, player_2, status;
	
	private final String STATUS_PROGRESS = "status.progress", STATUS_WAITING = "status.waiting", STATUS_NEW = "status.new", STATUS_VALIDATION = "status.validation", STATUS_FINISH = "status.finish";
	private final String PLAYER_UNDEFINED = "player.undefined";
	private final String PLAYER1 = "player_1", PLAYER2 = "player_2", STATUS = "game.status";
	private final String INFO_NEW_CREATION = "info.creation", INFO_VALIDATION = "info.validation", INFO_WAITING = "info.waiting", UNAUTHORIZED_USER_ERROR = "user.error";
	private final String WINNER = "game.winner", LOSER = "game.loser", TIE = "game.tie", VALIDATION = "game.validation";
	private final String REFUSE_MATCH = "refuse.match";
	
	private ResourceBundle resources = null;
	private Scheduler scheduler = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
		
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException error) {
			error.printStackTrace();
			App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
		}
		
		this.setPlayer1();
		this.setPlayer2();
		this.setStatusTagAndNotice();
		
		super.getItem().getPlayer_1Property().addListener(_ -> {
			this.setPlayer1();
			this.notice.setVisible(true);
		});
		super.getItem().getPlayer_2Property().addListener(_ -> {
			this.setPlayer2();
			this.notice.setVisible(true);
		});
		super.getItem().getStatusProperty().addListener(_ -> this.setStatusTagAndNotice());
		super.getItem().getSteps().addListener(new ListChangeListener<>() {
			@Override
			public void onChanged(Change<? extends Step> change) {
				if(change.wasAdded())
					notice.setVisible(true);
			}
		});
		
		super.selectedProperty().addListener((_, _, isSelected) -> {
			if(isSelected)  {
				ObjectAccessController.setCurrMatch(super.getItem());
				
				if(super.getItem().getStatus().compareTo("4") == 0) {
					this.notice.setVisible(false);
					App.crt_dlg("info_dialog", new GenericDialogController(resources.getString(INFO_NEW_CREATION)));
				} else if(super.getItem().getStatus().compareTo("3") == 0) {
					this.notice.setVisible(false);
					App.crt_dlg("info_dialog", new GenericDialogController(resources.getString(INFO_VALIDATION)));
				} else if(super.getItem().getStatus().compareTo("2") == 0) {
					this.notice.setVisible(false);
					App.crt_dlg("info_dialog", new GenericDialogController(resources.getString(INFO_WAITING)));
				} else if(super.getItem().getStatus().compareTo("1") == 0)
					this.notice.setVisible(false);
			}
		});
	}
	
	private void setPlayer1() {
		if(super.getItem().player_1_isUndefined())
			this.player_1.setText(resources.getString(PLAYER1) + " " + resources.getString(PLAYER_UNDEFINED));
		else
			this.player_1.setText(resources.getString(PLAYER1) + " " + super.getItem().getPlayer_1());
	}
	
	private void setPlayer2() {
		if(super.getItem().player_2_isUndefined())
			this.player_2.setText(resources.getString(PLAYER2) + " " + resources.getString(PLAYER_UNDEFINED));
		else
			this.player_2.setText(resources.getString(PLAYER2) + " " + super.getItem().getPlayer_2());
	}
	
	private void setStatusTagAndNotice() {
		this.notice.setVisible(true);
		if(super.getItem().getStatus().compareTo("1") == 0) {
			this.status.setText(this.resources.getString(STATUS) + " " + this.resources.getString(STATUS_PROGRESS));
			this.tag.setStyle("-fx-background-color: #32CD32;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
		} else if(super.getItem().getStatus().compareTo("2") == 0) {
			this.status.setText(this.resources.getString(STATUS) + " " + this.resources.getString(STATUS_WAITING));
			this.tag.setStyle("-fx-background-color: #FFA500;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
			if(User.get_usr_inst().getUsername().compareTo(super.getItem().getPlayer_1()) == 0)
				this.startScheduler();
			else {
				App.crt_dlg("info-dialog", new GenericDialogController(this.resources.getString(REFUSE_MATCH) + " " + super.getItem().getPlayer_1()));
				ObjectAccessController.getMatches().remove(super.getItem());
			}
		} else if(super.getItem().getStatus().compareTo("3") == 0) {
			this.status.setText(this.resources.getString(STATUS) + " " + this.resources.getString(STATUS_VALIDATION));
			this.tag.setStyle("-fx-background-color: #000000;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
			if(User.get_usr_inst().getUsername().compareTo(super.getItem().getPlayer_2()) == 0)
				this.startScheduler();
			else {
				ObjectAccessController.setCurrMatch(super.getItem());
				try {
					if(this.scheduler.checkExists(new JobKey("update", "game")))
						this.scheduler.shutdown();
				} catch (SchedulerException error) {
					error.printStackTrace();
					App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
				}
				App.crt_dlg("validation_dialog", new ValidationDialogController(this.resources.getString(VALIDATION) + super.getItem().getPlayer_2() + "?"));
			}
		} else if(super.getItem().getStatus().compareTo("4") == 0) {
			this.status.setText(this.resources.getString(STATUS) + " " + this.resources.getString(STATUS_NEW));
			this.tag.setStyle("-fx-background-color: #1E90FF;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
		} else if(super.getItem().getStatus().compareTo("0") == 0) {
			this.status.setText(this.resources.getString(STATUS) + " " + this.resources.getString(STATUS_FINISH));
			this.tag.setStyle("-fx-background-color: #FFFFFF;-fx-background-radius: 7 0 0 7;-fx-border-radius: 7 0 0 7;-fx-border-color: none;");
			try {
				if(this.scheduler.checkExists(new JobKey("update", "game")))
					this.scheduler.shutdown();
			} catch(SchedulerException error) {
				error.printStackTrace();
				App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
			}
			
			/* Gestisce la vittoria la sconfitta e il pareggio */
			if((super.getItem().getResult().compareTo("1") == 0 && super.getItem().getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0) || (super.getItem().getResult().compareTo("2") == 0 && super.getItem().getPlayer_2().compareTo(User.get_usr_inst().getUsername()) == 0)) {
				try {
					int status_code = HttpConnection.winner_request(super.getItem().getMatch_id());
					if(status_code == 401)
						throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
				} catch (Exception error) {
					error.printStackTrace();
					App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
				}
				
				App.crt_dlg("result_dialog", new GenericDialogController(this.resources.getString(WINNER)));
			} else if(super.getItem().getResult().compareTo("0") == 0 && super.getItem().getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0) {
				try {
					int status_code = HttpConnection.tie_request(super.getItem().getMatch_id());
					if(status_code == 401)
						throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
				} catch (Exception error) {
					error.printStackTrace();
					App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
				}
				
				App.crt_dlg("result_dialog", new GenericDialogController(this.resources.getString(TIE)));
			} else
				App.crt_dlg("result_dialog", new GenericDialogController(this.resources.getString(LOSER)));
			
			Stat stat = new Stat(super.getItem().getPlayer_1(), super.getItem().getPlayer_2(), super.getItem().getResult());
			ObjectAccessController.getStats().add(stat);
			ObjectAccessController.getMatches().remove(super.getItem());
		}
	}
	
	private void startScheduler() {
		try {
			if(!this.scheduler.checkExists(new JobKey("update", "game"))) {
				/* Creazione del Job da eseguire */
				JobDetail job = JobBuilder.newJob(GameListCellController.class).withIdentity("update", "game").build();
				/* Creazione del trigger che esegue il  Job ogni 30 secondi */
				SimpleTrigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("update_trigger", "game")
						.withSchedule(SimpleScheduleBuilder
								.simpleSchedule()
								.withIntervalInSeconds(30)
								.repeatForever()
						).build();
				/* Registrazione del job e del trigger */
				this.scheduler.scheduleJob(job, trigger);
			}
		} catch (SchedulerException error) {
			error.printStackTrace();
			App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
		}
	}
	
	@Override
	protected void updateItem(Match match, boolean empty) {
		super.updateItem(match, empty);
		
		try {
			if(match == null || empty)
				super.setGraphic(null);
			else
				super.setGraphic(App.load_cell("game_list_cell", this));
		} catch(IOException error) {
			error.printStackTrace();
			super.setGraphic(null);
			App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
		}
	}
	
	/* Viene chiamata ogni volta che parte un Job ed esegue le azioni al suo interno */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			int status_code = HttpConnection.update_request(super.getItem());
			if(status_code == 401)
				throw new Exception(resources.getString(UNAUTHORIZED_USER_ERROR));
		} catch (Exception error) {
			error.printStackTrace();
			App.crt_dlg("error_dialog", new GenericDialogController(error.getMessage()));
		}
	}

}
