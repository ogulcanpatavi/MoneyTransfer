# MoneyTransfer

Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.


Once the project is imported, make sure to update the project (maven -> update project).

To create the jar:
1) Run as -> maven clean
2) Run as -> maven install

This will create money_transfer-0.0.1-SNAPSHOT-jar-with-dependencies.jar under target folder.

To run the application as standalone:
1) Go to target folder
2) Open Terminal/Command Line
3) Run: java -jar money_transfer-0.0.1-SNAPSHOT-jar-with-dependencies.jar 


To easily validate the business logic, you can access to all available accounts including their account number, balance, currency via http://localhost:8080/admin/accounts

Available Account Numbers:
000001
000002
000003
000004
000005
000006
000007
000008
000009
000010

The application only supports application/json.

Money Transfer API: http://localhost:8080/payment/transfer

Sample Payload:
{
	"fromAcctNum" :"000001" ,
	"toAcctNum" : "000002",
	"currency" : 840,
	"amount" : 100,
	"notes" : "Coffee"
}

Note: If the currency given in the request is different than the currency of the toAcctNum, you will get an error message with the correct currency. Please modify the payload.
