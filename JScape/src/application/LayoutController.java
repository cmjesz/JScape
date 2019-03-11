package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Worker.State;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class LayoutController implements Initializable {

	// UI Control Identity Variables
    @FXML
    private Button forwardButton, backButton;
    @FXML
    private TextField urlField;
    @FXML
    private StackPane webViewStackPane;
    
    
    // Global Variables
    private BrowserLogic browserLogic;
	private double xPositionMove, yPositionMove;
	private double xPositionResize, yPositionResize;
	private String resizeDirection = "";

	
	/*
	 * Event methods used for handling web navigation
	 * 
	 * 
	 * 
	 */
	@FXML
	void urlFieldEnterPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			String url = browserLogic.navigate(urlField.getText());
	    	handleUIUpdate(url);
		}
	}
	
    @FXML
    void backButtonClicked(MouseEvent event) {
    	String url = browserLogic.navigateBackward();
    	handleUIUpdate(url);
    }

    @FXML
    void forwardButtonClicked(MouseEvent event) {
    	String url = browserLogic.navigateForward();
    	handleUIUpdate(url);
    }

    @FXML
    void homeButtonClicked(MouseEvent event) {
    	String url = browserLogic.navigateHome();
    	handleUIUpdate(url);
    }

    @FXML
    void refreshButtonClicked(MouseEvent event) {
    	String url = browserLogic.refresh();
    	handleUIUpdate(url);
    }
    
    
	
	/*
	 * Event methods used for window manipulation operations
	 * 
	 * 
	 * 
	 */
    @FXML
    void windowMoved(MouseEvent event) {
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setX(event.getScreenX() - xPositionMove);
    	stage.setY(event.getScreenY() - yPositionMove);
    }

    @FXML
    void windowPressed(MouseEvent event) {
    	xPositionMove = event.getSceneX();
    	yPositionMove = event.getSceneY();
    }
    
    @FXML
    void windowMinimized(MouseEvent event) {
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setIconified(true);
    }

    @FXML
    void windowMaximized(MouseEvent event) {
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    void windowClosed(MouseEvent event) {
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.close();
    }
    
    @FXML
    void updateCursor(MouseEvent event) {
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	((Node) event.getSource()).getScene().setCursor(getCursorType(event.getSceneX(), event.getSceneY(), stage.getWidth(), stage.getHeight()));
    }
    
    @FXML
    void windowResizeClick(MouseEvent event) {
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	xPositionResize = stage.getWidth() - event.getSceneX();
    	yPositionResize = stage.getHeight() - event.getSceneY();
    }
    
    @FXML
    void windowResizeRelease(MouseEvent event) {
    	resizeDirection = "";
    	((Node) event.getSource()).getScene().setCursor(Cursor.DEFAULT);
    }
    
    @FXML 
    void windowResizeDrag(MouseEvent event) {
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	switch (resizeDirection) {
    		case "NE":
    			stage.setWidth(event.getSceneX() + xPositionResize);
    			stage.setHeight(stage.getY() - event.getScreenY() + stage.getHeight());
                stage.setY(event.getScreenY());
    			break;
    		case "NW":
    			stage.setHeight(stage.getY() - event.getScreenY() + stage.getHeight());
                stage.setY(event.getScreenY());
    			stage.setWidth(stage.getX() - event.getScreenX() + stage.getWidth());
                stage.setX(event.getScreenX());
    			break;
    		case "SE":
    			stage.setHeight(event.getSceneY() + yPositionResize);
    			stage.setWidth(event.getSceneX() + xPositionResize);
    			break;
    		case "SW":
    			stage.setHeight(event.getSceneY() + yPositionResize);
    			stage.setWidth(stage.getX() - event.getScreenX() + stage.getWidth());
                stage.setX(event.getScreenX());
    			break;
    		case "N":
    			stage.setHeight(stage.getY() - event.getScreenY() + stage.getHeight());
                stage.setY(event.getScreenY());
    			break;
    		case "S":
    			stage.setHeight(event.getSceneY() + yPositionResize);
    			break;
    		case "E":
    			stage.setWidth(event.getSceneX() + xPositionResize);
    			break;
    		case "W":
    			stage.setWidth(stage.getX() - event.getScreenX() + stage.getWidth());
                stage.setX(event.getScreenX());
    			break;
			default:
    			// System.out.println("Invalid resizeDirection: " + resizeDirection);
    			break;
    	}
    }
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    WebView web = new WebView();
	    WebEngine browser = web.getEngine();
		webViewStackPane.getChildren().add(web);
		
		browserLogic = new BrowserLogic(browser);
		
		browser.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					String location = browser.getLocation();
					browserLogic.navigationSuccessful(location);
					handleUIUpdate(location);
				}
			}
		});
	}
	

	/*
	 * Helper methods
	 * 
	 * 
	 * 
	 */
	private Cursor getCursorType(double x, double y, double screenWidth, double screenHeight) {
		if (x <= 5 && y <= 5) {
			resizeDirection = "NW";
			return Cursor.NW_RESIZE;
		}
		else if (x >= screenWidth - 5 && y <= 5) {
			resizeDirection = "NE";
			return Cursor.NE_RESIZE;
		}
		else if (x <= 5 && y >= screenHeight - 5) {
			resizeDirection = "SW";
			return Cursor.SW_RESIZE;
		}
		else if (x >= screenWidth - 5 && y >= screenHeight -5) {
			resizeDirection = "SE";
			return Cursor.SE_RESIZE;
		}
		else if (x <= 5 || x >= screenWidth - 5) {
			if (x <= 5) {
				resizeDirection = "W";
			}
			else {
				resizeDirection = "E";
			}
			return Cursor.H_RESIZE;
		}
		else if (y <= 5 || y >= screenHeight - 5) {
			if (y <= 5) {
				resizeDirection = "N";
			}
			else {
				resizeDirection = "S";
			}
			return Cursor.V_RESIZE;
		}
		
		resizeDirection = "";
		return Cursor.DEFAULT;
	}
    
    private void handleUIUpdate(String url) {
    	backButton.setDisable(browserLogic.shouldBackwardBeDisabled());
    	forwardButton.setDisable(browserLogic.shouldForwardBeDisabled());
    	if (!url.isEmpty()) {
    		urlField.setText(url);
    	}
    }
}
