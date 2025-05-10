module org.javafx.trismasterfx {
	exports org.javafx.trismasterfx;
	exports org.javafx.trismasterfx.controller;
	exports org.javafx.trismasterfx.entity;
	
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires com.fasterxml.jackson.databind;
	requires org.apache.httpcomponents.client5.httpclient5.fluent;
	requires org.apache.httpcomponents.core5.httpcore5;
	requires org.apache.httpcomponents.client5.httpclient5;
	
	opens org.javafx.trismasterfx.controller to javafx.fxml;
	opens org.javafx.trismasterfx.entity to com.fasterxml.jackson.databind;
}
