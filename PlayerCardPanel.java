/*
Aaron Kimbel
ICS4U0-C
Final Project
Animal Kingdom: Card Arena
A Version of the card object that can be moved by the player
*/

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class PlayerCardPanel extends CardPanel {
    private boolean pressed = false;
    public PlayerCardPanel(Card card) {
        super(card);
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == 1) { //left mouse
                    DropLocation d = onDropLocation(); //if the card is on a drop zone, dont activate it
                    if(d == null) {
                        pressed = true;
                    }
                }
                if(e.getButton() == 3) { //right mouse
                    attackAnimation();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == 1) {
                    pressed = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    private boolean latch;
    private Point desiredLocation;
    public void update() {
        super.update();
        if(PanelManager.mouse != null) { //mouse is null if off screen
            if(desiredLocation == null) { //initialize point
                desiredLocation = new Point(x,y);
            }
            if(pressed) { //if the card is clicked
                if(!latch) { //called only once as soon as the card is clicked
                    latch = true;
                    PanelManager.board.moveUp(); //show the play area
                } else {
                    this.x = PanelManager.mouse.x - width/2;
                    this.y = PanelManager.mouse.y - height/2;
                }
            } else { //if the card is not clicked
                if(latch) { //called once when the card is dropped
                    latch = false;
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            PanelManager.board.moveDown(); //show the hand after short delay
                        }
                    }, 150);

                    DropLocation d = onDropLocation();
                    if(d != null) { //if the card is on a drop location
                        if(d.active) { //if the drop location is already occupied
                            d = null; //set the drop location as invalid
                        }
                    }
                    if(d != null) { //set desired location to the drop point
                        desiredLocation.x = d.x;
                        desiredLocation.y = d.y;
                        d.active = true; //activate the drop location
                        for(int x = 0; x < PanelManager.PlayerHand.length; x++) {
                            if (PanelManager.PlayerHand[x].equals(this)) {
                                PanelManager.PlayerHand[x] = this;
                                PanelManager.board.drawNewCard(x);
                                PanelManager.PlayerPlayedCards[d.index] = this;
                            }
                        }
                    } else { //return to start location
                        desiredLocation.x = startX;
                        desiredLocation.y = startY;
                    }
                    smoothTransition(desiredLocation); //smoothly go from current location to desired location
                }
            }
        }
        this.setBounds(x,y,width,height); //update cards x and y
    }

    public void destroy() {
        JPanel parent = (JPanel) this.getParent(); //get the parent panel
        DropLocation d = onDropLocation(); //get the cards drop location
        if(d != null) d.active = false; //if the card is on a drop location set it to be open
        parent.remove(this); //remove the card
        parent.revalidate();
        parent.repaint();
        for(int x = 0; x < PanelManager.PlayerPlayedCards.length; x++) {
            if (PanelManager.PlayerPlayedCards[x].equals(this)) {
                PanelManager.PlayerPlayedCards[x] = null;
            }
        }
    }

    public void attackAnimation() { //moves the card up then back down
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean latch = false;
            int lastY = y;
            @Override
            public void run() {
                if(!latch) y = (int) PanelManager.lerp(y,startY-50,0.1);
                else y = (int) PanelManager.lerp(y, startY, 0.1);
                if(y == lastY) {
                    if(!latch) latch = true;
                    else {
                        y = startY;
                        this.cancel();
                    }
                }
                lastY = y;
            }
        }, 1, 10);
    }

    public DropLocation onDropLocation() { //returns the DropLocation the card is hovering over, null if none
        Rectangle card = new Rectangle(x,y,width,height);
        for(DropLocation d : PanelManager.dropLocations) {
            Rectangle drop = new Rectangle(d.x,d.y,d.width,d.height);
            if(card.intersects(drop)) {
                return d;
            }
        }
        return null;
    }

}
