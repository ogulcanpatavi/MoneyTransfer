package com.revolut.money_transfer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.revolut.money_transfer.controller.AdminController;
import com.revolut.money_transfer.controller.TransferController;
import com.revolut.money_transfer.exception.DataNotAvailableExceptionMapper;
import com.revolut.money_transfer.exception.InsufficientBalanceExceptionMapper;
import com.revolut.money_transfer.exception.TransferCompletionExceptionMapper;
import com.revolut.money_transfer.utilities.DBConnection;

public class App {

	private final static Logger LOGGER = Logger.getLogger(App.class.getName());

	public static void main(String[] args) throws Exception {
		startServer();
		insertData();
	}

	private static void startServer() throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(8080);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				TransferController.class.getCanonicalName() + "," + AdminController.class.getCanonicalName() + ","
						+ DataNotAvailableExceptionMapper.class.getCanonicalName() + "," + InsufficientBalanceExceptionMapper.class.getCanonicalName() + ","
						+ TransferCompletionExceptionMapper.class.getCanonicalName());

		try {
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}

	}

	private static void insertData() throws SQLException {
		Connection connection = DBConnection.getDBConnection();
		Statement stmt = null;
		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			stmt.execute(
					"CREATE TABLE Account (AcctNum varchar(40) NOT NULL, Currency int NOT NULL, Balance decimal(15,2) NOT NULL, PRIMARY KEY (AcctNum));");
			stmt.execute(
					"CREATE TABLE FxRates (FromCurrency int NOT NULL, ToCurrency int NOT NULL, Rate decimal(10,6) NOT NULL);");
			stmt.execute("INSERT INTO FxRates VALUES (840, 826, 0.786522);");
			stmt.execute("INSERT INTO FxRates VALUES (840, 978, 0.876420);");
			stmt.execute("INSERT INTO FxRates VALUES (840, 949, 5.28197);");
			stmt.execute("INSERT INTO FxRates VALUES (840, 756, 0.988135);");
			stmt.execute("INSERT INTO FxRates VALUES (826, 840, 1.27142);");
			stmt.execute("INSERT INTO FxRates VALUES (826, 978, 1.11443);");
			stmt.execute("INSERT INTO FxRates VALUES (826, 949, 6.71806);");
			stmt.execute("INSERT INTO FxRates VALUES (826, 756, 1.25423);");
			stmt.execute("INSERT INTO FxRates VALUES (978, 840, 1.14101);");
			stmt.execute("INSERT INTO FxRates VALUES (978, 826, 0.897318);");
			stmt.execute("INSERT INTO FxRates VALUES (978, 949, 6.03107);");
			stmt.execute("INSERT INTO FxRates VALUES (978, 756,1.12875);");
			stmt.execute("INSERT INTO FxRates VALUES (949, 840, 0.189323);");
			stmt.execute("INSERT INTO FxRates VALUES (949, 826, 0.148852);");
			stmt.execute("INSERT INTO FxRates VALUES (949, 978, 0.165808);");
			stmt.execute("INSERT INTO FxRates VALUES (949, 756, 0.187175);");
			stmt.execute("INSERT INTO FxRates VALUES (756, 840, 1.01201);");
			stmt.execute("INSERT INTO FxRates VALUES (756, 978, 0.885936);");
			stmt.execute("INSERT INTO FxRates VALUES (756, 949,  5.34260);");
			stmt.execute("INSERT INTO FxRates VALUES (756, 826, 0.797301);");
			stmt.execute("INSERT INTO Account VALUES ('agOJAKXDmnb/zNoyJs+KYQ==',840,50000);");
			stmt.execute("INSERT INTO Account VALUES ('ehhJ1xODAfDb76XE1va/xg==',840,7000);");
			stmt.execute("INSERT INTO Account VALUES ('MNwwyKZJ/3r129XIoHk8aQ==',826,8000);");
			stmt.execute("INSERT INTO Account VALUES ('2c9bKRMXGgnbtcVQs8RomA==',826,2000);");
			stmt.execute("INSERT INTO Account VALUES ('OgSBtTUmvDOpoKkgI65q4g==',826,70000);");
			stmt.execute("INSERT INTO Account VALUES ('FIQ5wsACCG1JEh4ZeanMjA==',978,50000);");
			stmt.execute("INSERT INTO Account VALUES ('jERUAfv5LoWwmhbxMfGj8w==',978,2300);");
			stmt.execute("INSERT INTO Account VALUES ('mNw/KwASXIs8KqQJNeXTjQ==',949,55000);");
			stmt.execute("INSERT INTO Account VALUES ('Bb1GzB5Co5kyMz4xYlXCqg==',978,34000);");
			stmt.execute("INSERT INTO Account VALUES ('87Szqv8bw6+t05wwL+Fogw==',756,760000);");

			stmt.close();
			connection.commit();
			LOGGER.warning("Data is loaded.");
		} catch (SQLException e) {
			LOGGER.warning("Exception Message " + e.getMessage());
		} finally {
			connection.close();
		}

	}
}
