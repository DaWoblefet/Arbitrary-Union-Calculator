/* Written by Leonard Craft III (DaWoblefet) */

package arbitraryUnionCalculator;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.geometry.Insets;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class UnionCalculatorGUI extends Application
{
	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	private final ClipboardContent content = new ClipboardContent();

	@Override
	public void start(Stage primaryStage)
	{
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(5, 2, 5, 2));

		/* Top */
		BorderPane top = new BorderPane();
		top.setPadding(new Insets(0, 0, 5, 0));

		Label instructionsLabel = new Label("Please enter probabilities as fractions (with a \"/\") or as decimals.");
		Button helpButton = new Button("Help");
		top.setLeft(instructionsLabel);
		top.setRight(helpButton);

		pane.setTop(top);

		/* Probability set entry, center */
		GridPane setInput = new GridPane();

		ArrayList<Label> probabilitySetLabels = new ArrayList<Label>();
		ArrayList<TextField> probabilitySetTFs = new ArrayList<TextField>();

		for (int i = 0; i < 2; i++)
		{
			probabilitySetLabels.add(new Label(" Probability " + (i + 1) + ": "));
			probabilitySetTFs.add(new TextField());
			probabilitySetTFs.get(i).setPrefWidth(550);

			setInput.setConstraints(probabilitySetLabels.get(i), 0, i);
			setInput.setConstraints(probabilitySetTFs.get(i), 1, i);

			setInput.getChildren().addAll(probabilitySetLabels.get(i), probabilitySetTFs.get(i));
		}

		GridPane addRemoveButtons = new GridPane();

		Button addRowButton = new Button("+");
		Button removeRowButton = new Button("-");
		removeRowButton.setPrefWidth(25);

		addRemoveButtons.addRow(1, addRowButton, removeRowButton);
		addRemoveButtons.setHgap(5);

		setInput.setConstraints(addRemoveButtons, 0, 2);
		setInput.getChildren().add(addRemoveButtons);

		setInput.setVgap(2);
		pane.setCenter(setInput);

		/* Results, Bottom */
		GridPane results = new GridPane();

		Button calcButton = new Button("Calculate Union");
		results.setConstraints(calcButton, 0, 0);

		Label percentLabel = new Label(" Percent of the time:");
		TextField percentTF = new TextField();
		percentTF.setEditable(false);
		percentTF.setPrefWidth(450);
		Button copyPercent = new Button("Copy");

		Label rawLabel = new Label(" Raw fraction:");
		TextField rawTF = new TextField();
		rawTF.setEditable(false);
		Button copyRaw = new Button("Copy");

		Label reducedLabel = new Label(" Simplified fraction:");
		TextField reducedTF = new TextField();
		reducedTF.setEditable(false);
		Button copyReduced = new Button("Copy");

		results.setConstraints(percentLabel, 0, 1);
		results.setConstraints(percentTF, 1, 1);
		results.setConstraints(copyPercent, 2, 1);
		results.setConstraints(rawLabel, 0, 2);
		results.setConstraints(rawTF, 1, 2);
		results.setConstraints(copyRaw, 2, 2);
		results.setConstraints(reducedLabel, 0, 3);
		results.setConstraints(reducedTF, 1, 3);
		results.setConstraints(copyReduced, 2, 3);

		results.getChildren().addAll(calcButton, percentLabel, percentTF, copyPercent, rawLabel, rawTF, copyRaw, reducedLabel, reducedTF, copyReduced);
		results.setHgap(5);
		pane.setBottom(results);

		addRowButton.setOnAction(e -> {
			probabilitySetLabels.add(new Label(" Probability " + (probabilitySetLabels.size() + 1) + ": "));
			probabilitySetTFs.add(new TextField());

			int index = probabilitySetTFs.size() - 1;
			probabilitySetTFs.get(index).setPrefWidth(550);

			setInput.setConstraints(probabilitySetLabels.get(index), 0, index);
			setInput.setConstraints(probabilitySetTFs.get(index), 1, index);
			setInput.setConstraints(addRemoveButtons, 0, index + 1);

			setInput.getChildren().addAll(probabilitySetLabels.get(index), probabilitySetTFs.get(index));
		});

		removeRowButton.setOnAction(e -> {
			int index = probabilitySetTFs.size() - 1;

			setInput.getChildren().remove(probabilitySetLabels.get(index));
			setInput.getChildren().remove(probabilitySetTFs.get(index));

			probabilitySetLabels.remove(index);
			probabilitySetTFs.remove(index);

			setInput.setConstraints(addRemoveButtons, 0, index);
		});

		calcButton.setOnAction(e -> {

			BigFraction[] probabilitySets = new BigFraction[probabilitySetTFs.size()];

			for (int i = 0; i < probabilitySetTFs.size(); i++)
			{
				probabilitySets[i] = parseInputType(probabilitySetTFs.get(i).getText());
			}

			BigFraction result = BigFraction.calculateArbitraryUnion(probabilitySets);

			percentTF.setText(result.toPercent());
			rawTF.setText(result.toString());
			reducedTF.setText(result.reduce().toString());
		});

		helpButton.setOnAction(e -> {openHelp();});
		copyPercent.setOnAction(e -> {copyToClipboard(percentTF.getText());});
		copyRaw.setOnAction(e -> {copyToClipboard(rawTF.getText());});
		copyReduced.setOnAction(e -> {copyToClipboard(reducedTF.getText());});

		Scene scene = new Scene(pane, 650, 300);
		primaryStage.setTitle("Arbitrary Union Calculator");
		Image icon = new Image("file:.\\resources\\woblescientist.png");
		primaryStage.getIcons().add(icon);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void copyToClipboard(String data)
	{
		content.putString(data);
		clipboard.setContent(content);
		return;
	}

	public void openHelp()
	{
		String helpTextRaw = "";
		try
		{
			Scanner input = new Scanner(new File(".\\resources\\helptext.txt"));
			while (input.hasNextLine())
			{
				helpTextRaw += input.nextLine() + "\n";
			}
			input.close();
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}

		Stage stage = new Stage();
		TextArea helpText = new TextArea(helpTextRaw);
		helpText.setEditable(false);
		helpText.setWrapText(true);
		helpText.setPrefRowCount(19);

		Scene scene = new Scene(helpText, 500, 300);
		stage.setScene(scene);
		stage.setTitle("Help");
		stage.show();
		return;
	}

	public BigFraction parseInputType(String input)
	{
		BigFraction initialProb;
		if (input.contains(".")) //It's a decimal
		{
			initialProb = new BigFraction(input);
		}
		else //It's a fraction
		{
			String[] splitFraction = input.split("\\/");
			initialProb = new BigFraction(splitFraction[0], splitFraction[1]);
		}

		return initialProb;
	}
}
