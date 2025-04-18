module trismaster {
	exports com.maven.trismaster;
	exports com.maven.trismaster.controller;
	exports com.maven.trismaster.dao;
	exports com.maven.trismaster.entity;
	
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires com.fasterxml.jackson.databind;
	requires org.apache.httpcomponents.client5.httpclient5.fluent;
	requires org.apache.httpcomponents.core5.httpcore5;
	requires org.apache.httpcomponents.client5.httpclient5;

	opens com.maven.trismaster.controller to javafx.fxml;
}