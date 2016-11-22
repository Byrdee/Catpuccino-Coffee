/**
 * This program allows the user to place an order for tea or coffee in 
 * several different increments and of different types.
 * It allows the user to print out their receipt if they would like and allows
 * another order to be placed without needing to be closed and reopened.
 */
package catpuccinocoffee;

import java.text.DecimalFormat;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class CatpuccinoCoffee extends Application
{
	public VBox CoffeePanel, BeanPanel, ImageBox;
	public GridPane AmountDue, MainPane;
	public HBox CartButtonPanel, TypePanel;
	public Button AddCartButton, EmptyCartButton, PlaceOrderButton;
	public RadioButton Coffee, Tea;
	public RadioButton Whole, Ground;
	public ComboBox PickAmount;
	public ComboBox PickBlend;
	public Label Tax, SubTotal, Total, Cart, TaxAmt, SubTotalAmt, TotalAmt;
	public ListView<String> CartItems;
	public Label  TypeOr, BeanOr;
	public ToggleGroup Bean, Type;
	public EventHandler<ActionEvent> Handler;
	public ObservableList<String> ShoppingCart;
	double TotalI, SubTotalI, TaxI;
	final int[] Prices = {5, 10, 20, 25, 30, 35};
	public DecimalFormat DF = new DecimalFormat("#.00");
	public double Payment;

	
	public static void main(String[] args) throws IOException
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage)
	{
		Builder();
		Scene scene = new Scene(MainPane, 800, 600);
		scene.getStylesheets().add("/catpuccinocoffee/StyleSheet.css");
		stage.setTitle("Welcome to Catpuccino Coffee");
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }

        });
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Builder initializes and builds the GUI for the program.
	 * It also has the event handlers for the radio buttons
	 * 
	 * There are no parameters and the method does not return
	 */
	public void Builder()
	{		
		TotalI = 0.0;
		SubTotalI = 0.0;
		TaxI = 0.0;
		
		AddCartButton = new Button("Add to Cart");
		AddCartButton.setOnAction(new ButtonClickHandler());
		//AddCartButton.setStyle("-fx-background-color: #deb576;");
		AddCartButton.getStyleClass().add("button");
		
		PickBlend = new ComboBox();
		String[] Blend = {"Dark Roast", "Medium Roast", "Light Roast",
				"Hazelnut Roast", "French Roast"};

		String[] Leaf = {"Green Tea", "White Tea", "Chai Tea", "Black Tea",
				"Oolong Tea"};
		
		PickAmount = new ComboBox();
		
		String[] CoffeeLbs = {"1/2lb", "1lb", "2lbs", "3lbs", "4lbs", "5lbs"};
		String[] TeaLbs = {"1/2lb", "1lb", "2lbs"};

		
		Type = new ToggleGroup();
		Coffee = new RadioButton("Coffee");
		TypeOr = new Label(" Or  ");
		Tea = new RadioButton("Tea");
		Coffee.setToggleGroup(Type);
		Tea.setToggleGroup(Type);
		Type.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				
			@Override
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle OldT, Toggle NewT)
			{
				if (Type.getSelectedToggle() == Coffee) //Coffee is a radio button
				{
					BeanPanel.setVisible(true);
					PickBlend.setPromptText("Select Coffee");
					PickBlend.getItems().clear();
					for(String S : Blend)
						PickBlend.getItems().add(S);
					
					PickAmount.setPromptText("Select Amount");
					PickAmount.getItems().clear();
					for(String S : CoffeeLbs)
						PickAmount.getItems().add(S);


				}
				if (Type.getSelectedToggle() == Tea)  // Tea is a radio
				{
					BeanPanel.setVisible(false);
					PickBlend.setPromptText("Select Tea Type");
					PickBlend.getItems().clear();
					for(String S : Leaf)
						PickBlend.getItems().add(S);
						
					PickAmount.setPromptText("Select Amount");
					PickAmount.getItems().clear();
					for(String S : TeaLbs)
						PickAmount.getItems().add(S);
				}
				
			}
		});
		
		TypePanel = new HBox();
		TypePanel.setAlignment(Pos.CENTER);
		TypePanel.getChildren().addAll(Coffee, TypeOr, Tea);
		
		Bean = new ToggleGroup();
		Whole = new RadioButton("Whole");
		BeanOr = new Label("Or");
		Ground = new RadioButton("Ground");
		Whole.setToggleGroup(Bean);
		Ground.setToggleGroup(Bean);
		BeanPanel = new VBox();
		BeanPanel.setAlignment(Pos.CENTER);
		BeanPanel.getChildren().addAll(Whole, BeanOr, Ground);
		BeanPanel.setBorder(new Border(new BorderStroke(Color.BURLYWOOD,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		
		Tax = new Label("Tax(7%):");
		Tax.setTextAlignment(TextAlignment.LEFT);
		SubTotal = new Label("Subtotal:");
		SubTotal.setTextAlignment(TextAlignment.LEFT);
		Total = new Label("Total:");
		Total.setTextAlignment(TextAlignment.LEFT);
		TaxAmt = new Label("$00.00");
		TaxAmt.setTextAlignment(TextAlignment.RIGHT);
		SubTotalAmt = new Label("$00.00");
		SubTotalAmt.setTextAlignment(TextAlignment.RIGHT);
		TotalAmt = new Label("$00.00");
		TotalAmt.setTextAlignment(TextAlignment.RIGHT);
		AmountDue = new GridPane();
		AmountDue.setHgap(15);
		AmountDue.setVgap(8);
		AmountDue.add(Tax, 0, 0, 2, 1);
		AmountDue.add(TaxAmt, 2, 0);
		AmountDue.add(SubTotal, 0, 1, 2, 1);
		AmountDue.add(SubTotalAmt, 2, 1);
		AmountDue.add(Total, 0, 2, 2, 1);
		AmountDue.add(TotalAmt, 2, 2);
		
		Cart = new Label("Cart");
		Cart.setFont(Font.font(24));
		Cart.setUnderline(true);
		PlaceOrderButton = new Button("Place Order");
		EmptyCartButton = new Button("Empty Cart");
		PlaceOrderButton.setOnAction(new ButtonClickHandler());
		EmptyCartButton.setOnAction(new ButtonClickHandler());
		EmptyCartButton.setStyle("-fx-background-color: #deb576;");
		PlaceOrderButton.setStyle("-fx-background-color: #deb576;");
		
		CartItems = new ListView<>();
		CartItems.setPrefWidth(150);
		CartItems.setPrefHeight(150);
		ShoppingCart =FXCollections.observableArrayList();
	
		ImageBox = new VBox();
		ImageBox.setAlignment(Pos.CENTER);
		ImageBox.getChildren().add(new ImageView("file:\\D:\\School\\Java II\\Final Project\\CatpuccinoCoffee\\Logo.jpg"));
		
		// Sets the size for each of the 16 columns
		MainPane = new GridPane();
		for (int I = 0; I < 16; I++)
			MainPane.getColumnConstraints().add(new ColumnConstraints(50));
		// Sets the size for each of the 12 rows
		for (int I = 0; I < 12; I++)
			MainPane.getRowConstraints().add(new RowConstraints(50));

		 
		MainPane.add(ImageBox, 0, 0, 16, 3);
		MainPane.add(TypePanel,2, 3, 6, 1);
		MainPane.add(AddCartButton, 4, 11, 2, 1);
		MainPane.add(Cart, 12, 3, 1, 1);
		MainPane.add(CartItems, 10, 4, 5, 3);
		MainPane.add(AmountDue, 11, 8, 5, 4);
		MainPane.add(PlaceOrderButton, 10, 11, 2, 1);
		MainPane.add(EmptyCartButton, 13, 11, 2, 1);
		MainPane.setPrefSize(100, 100);
		MainPane.setMinSize(100, 100);
		MainPane.add(PickBlend, 1, 4, 4, 2);
		MainPane.add(PickAmount, 1, 6, 4, 2);
		MainPane.add(BeanPanel, 6, 5, 2, 2);
		BeanPanel.setVisible(false);
		MainPane.setStyle("-fx-background-color: #aa7843;");

	}
		
	
	/** 
	 * Button Click Handler is a private inner class
	 * It handles how the program responds when the 
	 * AddCartButton, EmptyCartButton and PlaceOrderButtons are clicked
	 * 
	 * This class either returns or calls other functions depending on
	 * which button has been clicked and if certain conditions have been met
	 * 
	 **/
	private class ButtonClickHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event) 
		{
			Button Clicked = (Button) event.getTarget();
					
			if (Clicked.equals(AddCartButton))
			{
				if (Coffee.isSelected())
				{						
					String TempCoffee = PickBlend.getSelectionModel().getSelectedItem().toString();
					if (Ground.isSelected())
					{
						
						int TempAmount = PickAmount.getSelectionModel().getSelectedIndex();
						AddToCart(TempCoffee, TempAmount, "Ground");
					}
					else if (Whole.isSelected())
					{
						int TempAmount = PickAmount.getSelectionModel().getSelectedIndex();
						AddToCart(TempCoffee, TempAmount, "Whole");
					}
					else
						JOptionPane.showMessageDialog(null, "You must select" +
								" either Whole or Ground for the beans.");
				}
				else if (Tea.isSelected())
				{
					String TempTea = PickBlend.getSelectionModel().getSelectedItem().toString();
					int TempAmount = PickAmount.getSelectionModel().getSelectedIndex();
					AddToCart(TempTea, TempAmount);
				}
				else
					JOptionPane.showMessageDialog(null, "You must select" +
							" an item to add to the cart first.");
				
			}
			else if (Clicked.equals(EmptyCartButton))
			{
				EmptyCart();
			}
			else if (Clicked.equals(PlaceOrderButton))
			{
				CheckOut();
			}
		}
	}
	
	/**
	 * An overloaded function that responds when the AddToCart button has been
	 * clicked. 
	 * @param T, String containing the type of coffee
	 * @param A, int used as an index to the array of Prices
	 * @param B, String holding the amount of coffee added
	 * 
	 * Does not return but calls the method to calculate the totals
	 */
	public void AddToCart(String T, int A, String B)
	{
		int P = Prices[A];
		SubTotalI+= P;
			
		//ShoppingCart =FXCollections.observableArrayList();
		ShoppingCart.add(T + " " + PickAmount.getSelectionModel().getSelectedItem() +
				" " + B + ", $" + Prices[A]);
		CartItems.setItems(ShoppingCart);
		CalculateTotals();
	}
	
	/**
	 * An overloaded function that responds when the AddToCart button has been
	 * clicked.
	 * 
	 * @param T, String containing the type of Tea
	 * @param A, int used as an index to the array of Prices
	 * 
	 * Does not return but calls the method to calculate the totals
	 */
	public void AddToCart(String T, int A)
	{
		int P = Prices[A];
		SubTotalI+= P;

		//ShoppingCart =FXCollections.observableArrayList();
		ShoppingCart.add(T + " " + PickAmount.getSelectionModel().getSelectedItem() +
				", $" + Prices[A]);
		CartItems.setItems(ShoppingCart);
		CalculateTotals();
	}
	
	/**
	 * Empties the ListView item, resets several items and calls the
	 * method CalculateTotals
	 */
	public void EmptyCart()
	{
		CartItems.getItems().clear();
		SubTotalI = 0.0;
		BeanPanel.setVisible(false);
		PickBlend.getItems().clear();
		PickAmount.getItems().clear();
		PickBlend.setPromptText("");
		PickAmount.setPromptText("");
		CalculateTotals();
	}
	
	/**
	 * Checkout guides the users through the process of checking out, including
	 * their payment, if they would like a receipt or not and how much change
	 * they are due.
	 * 
	 * Does not return, but recursively calls itself if input validation fails
	 * or calls the PrintReceipt or EmptyCart functions.
	 */
	public void CheckOut()
	{
		JFrame frame = new JFrame();
		String Cash = JOptionPane.showInputDialog("Your total is $" + TotalI +
				"\nPlease enter what you will be paying in cash:");

		if (isNumeric(Cash))
		{
			Payment = Double.parseDouble(Cash);
			double ChangeDue = Payment - TotalI;
			if (ChangeDue > 0)
			{
				int N = JOptionPane.showConfirmDialog(frame, "Change Due is $" + 
						DF.format(ChangeDue) + "\nWould you like your " +
						"receipt printed out?", "Receipt?", JOptionPane.YES_NO_OPTION);
				if (N == 0)
				{
				    try 
					{
						PrintReceipt();
				    }
					catch (Exception ex) 
					{
						JOptionPane.showMessageDialog(null, "Unable to print order information at this time.");
				    }
				}
				else
				{
					int X = JOptionPane.showConfirmDialog(frame, "Would you like to place another order?",
					"Place Another Order?", JOptionPane.YES_NO_OPTION);
					if (X == 0)
					{
						EmptyCart();
						Type.selectToggle(null);
						BeanPanel.setVisible(false);
						PickBlend.getItems().clear();
						PickAmount.getItems().clear();
						PickBlend.setPromptText("");
						PickAmount.setPromptText("");
						
						
					}
					else
					{
						System.exit(0);
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "That is not enough. Please  " + 
						"enter the cash again.");
				CheckOut();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please enter enter numbers or periods only.");
			CheckOut();
		}
		
	}
	
	public void CalculateTotals()
	{
		TaxI = SubTotalI * 0.07;
		TotalI = TaxI + SubTotalI;

		TaxAmt.setText("$" + DF.format(TaxI));
		SubTotalAmt.setText("$" + DF.format(SubTotalI));
		TotalAmt.setText("$" + DF.format(TotalI));
	}
	
	/**
	 * Code taken from StackOverflow.com, it determines if a given string is a
	 * double or not.
	 * 
	 * @param str, String that contains the value entered by the user as payment
	 * @return boolean true or false
	 */
	public static boolean isNumeric(String str)  
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true; 
	}
	
	/**
	 * Goes through the process of creating a saved .txt file containing the 
	 * date, user's name, items ordered, totals, payment and cash due. It uses
	 * the Printing class to print out the file for the user
	 * 
	 * @throws Exception 
	 */
	public void PrintReceipt() throws Exception
	{
		JFrame frame = new JFrame();
		DateFormat DateF = new SimpleDateFormat("MM/dd/yy");
		Date D = new Date();
		String Name = JOptionPane.showInputDialog(null, "Please enter your first and last name: ");
		PrintWriter NewFile = new PrintWriter(Name + " Order.txt");
		NewFile.println(DateF.format(D));
		NewFile.println(Name);
		
		ObservableList C = CartItems.getItems();
		for (int I = 0; I < C.size(); I++)
		{
			NewFile.println(C.get(I));
		}
		NewFile.println("\nTax(7%): $" + DF.format(TaxI));
		NewFile.println("Subtotal: $" + DF.format(SubTotalI));
		NewFile.println("Total: $" + DF.format(TotalI));
		NewFile.println("Cash Paid: $" + DF.format(Payment));
		NewFile.println("Change Due: $" + DF.format((Payment - TotalI)));
		
		NewFile.close();
		Printing PrintIt = new Printing(Name + " Order.txt");
		
		int N = JOptionPane.showConfirmDialog(frame, "Your receipt has been printed.\n" +
				"Would you like to place another order?",
				"Place Another Order?", JOptionPane.YES_NO_OPTION);
		if (N == 0)
		{
			EmptyCart();
			Type.selectToggle(null);
			BeanPanel.setVisible(false);
			PickBlend.getItems().clear();
			PickAmount.getItems().clear();
			PickBlend.setPromptText("");
			PickAmount.setPromptText("");
		}
		else
		{
			System.exit(0);
		}
	   
	}
	
}
