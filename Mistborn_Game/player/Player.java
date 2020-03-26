package player;

//graphics imports
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import java.util.HashSet;

import platforms.Platform;

import java.awt.event.KeyEvent;

public class Player {

    private double xPos = 0;
    private double yPos = 0;
    private double xSpeed = 0;
    private double ySpeed = 0;

    private double width = 50;
    private double height = 100;

    private double maxRunSpeed = 10;

    private double jumpHeightCount;

    private boolean canJump;

    private boolean falling = false;
    private boolean grounded = true;

    private boolean crouching;

    private State state; 

    public static HashSet<Integer> keysPressed = new HashSet<>();

    public Player() {
        this.state = State.IDLE;
    }

    public void tick() {
        /*this.readInputs();
        this.fall();
        this.move();
        this.collision();*/

        this.move();

        switch (state) {
            case IDLE:
                checkForHorizAcc();
                checkForJump();
                checkForCrouch();

                break;

            case WALKING:
                checkForHorizAcc();
                checkForJump();
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                checkForHorizCollision();

            break;

            case RUNNING:
                checkForHorizAcc();
                checkForJump();
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                checkForHorizCollision();

            break;

            case SLIDING:
                checkForHorizAcc();
                checkForJump();
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                checkForHorizCollision();

            break;

            case JUMPING:
                if (keysPressed.contains(KeyEvent.VK_UP) && this.jumpHeightCount < 5) {
                    this.ySpeed = -12;
                    this.jumpHeightCount++;
                } else {
                    this.state = State.FALLING;
                }
            break;

            case FALLING:
                checkForHorizAcc();
                fall();
                checkForVertCollision();
            break;

            case LANDING:
            break;

            case CROUCHING:
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                checkForHorizCollision();
            break;

            //TODO: Interacts weirdly, collision doesnt work from left side
            case AT_WALL:
                checkForHorizAcc();
                checkForJump();
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                checkForHorizCollision();
            break;
        }


    }

    //movement methods
    //-----------------------------------------------------------------------------------------------
    private void checkForHorizAcc() {
        //speed up to the left
        if (keysPressed.contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * maxRunSpeed) {
            this.xSpeed -= 1;
        }

        //speed up to the right
        if (keysPressed.contains(KeyEvent.VK_RIGHT) && this.xSpeed < maxRunSpeed) {
            this.xSpeed += 1;
        }

        //set new state
        if (this.grounded == true) {
            if (this.xSpeed == 0) {
                this.state = State.IDLE;
            } else if (Math.abs(this.xSpeed) < 5) {
                this.state = State.WALKING;
            } else {
                this.state = State.RUNNING;
            }
        } else {
            this.state = State.FALLING;
        }
    }

    private void checkForFriction() {
        if (!(keysPressed.contains(KeyEvent.VK_RIGHT)) && !(keysPressed.contains(KeyEvent.VK_LEFT))) {
            this.xSpeed *= 0.8;
            this.state = State.SLIDING;

            if (Math.abs(this.xSpeed) < 1) {
                this.xSpeed = 0;
                this.state = State.IDLE;
            }
        }
    }

    private void checkForJump() {
        //jump
        if (keysPressed.contains(KeyEvent.VK_UP) && this.canJump == true) {
            this.ySpeed = -12;

            this.canJump = false;
            this.grounded = false;
            this.falling = true;

            this.jumpHeightCount = 0;

            this.state = State.JUMPING;
        }
    }

    private void checkForCrouch() {
        if (keysPressed.contains(KeyEvent.VK_DOWN) && this.grounded == true) {
            this.state = State.CROUCHING;
        }
    }

    private void checkIfFalling() {
        this.yPos++;
        if (!collided()) {
            this.falling = true;
            this.canJump = false;
            this.grounded = false;

            this.state = State.FALLING;
        }

        this.yPos--;
    }

    //returns true if overlapping with a platform
    private boolean collided() {
        //check every platform for collision
        for (int platNum = 0; platNum < Platform.platforms.size(); platNum++) {
            Platform platform  = Platform.platforms.get(platNum);
            boolean onSameX = false;
            boolean onSameY = false;

            if (this.xPos + this.width >= platform.xPos && platform.xPos + platform.width >= this.xPos) {
                onSameX = true;
            }

            if (this.yPos + this.height >= platform.yPos && platform.yPos + platform.height >= this.yPos) {
                onSameY = true;
            }

            //if player is in the same space as any platform, return true
            if (onSameX && onSameY) {
                return true;
            }
        }
        //if it gets here, player is not in the same space as any platform, so return false
        return false;
    }

    private void checkForVertCollision() {
        if (collided()) {
            //move in the opposite direction of movement 1px at a time
            for (int yOffset = 0; yOffset < this.ySpeed; yOffset++) {
                if (this.ySpeed > 0) {
                    this.yPos--;
                } else {
                    this.yPos++;
                }

                //if player moves out of the platform, place her on the ground
                if (!collided()) {
                    this.ySpeed = 0;
                    this.grounded = true;
                    this.canJump = true;
                    this.falling = false;

                    //set state according to movement speed
                    if (this.xSpeed == 0) {
                        this.state = State.IDLE;
                    } else if (Math.abs(this.xSpeed) < 5) {
                        this.state = State.WALKING;
                    } else {
                        this.state = State.RUNNING;
                    }

                    break;
                }
            }
        }
    }

    private void checkForHorizCollision() {
        if (collided()) {
            //move in the opposite direction of movement 1px at a time
            for (int xOffset = 0; xOffset < this.xSpeed; xOffset++) {
                if (this.xSpeed > 0) {
                    this.xPos--;
                } else {
                    this.xPos++;
                }

                //if player moves out of wall, place her at the wall
                if (!collided()) {
                    this.xSpeed = 0;

                    this.state = State.AT_WALL;

                    break;
                }
            }
        }
    }
    
    private void readInputs() {
        //speed up to the left
        if (keysPressed.contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * maxRunSpeed) {
            this.xSpeed -= 1;
            this.state = State.RUNNING;
        }

        //speed up to the right
        if (keysPressed.contains(KeyEvent.VK_RIGHT) && this.xSpeed < maxRunSpeed) {
            this.xSpeed += 1;
            this.state = State.RUNNING;
        }

        //slow down
        if (!(keysPressed.contains(KeyEvent.VK_RIGHT)) && !(keysPressed.contains(KeyEvent.VK_LEFT))) {
            this.xSpeed *= 0.8;

            if (Math.abs(this.xSpeed) < 1) {
                this.xSpeed = 0;
                this.state = State.IDLE;
            }
        }

        //jump
        if (keysPressed.contains(KeyEvent.VK_UP) && this.canJump == true) {
            this.ySpeed = -9;            
            this.state = State.JUMPING;
        }

        if (keysPressed.contains(KeyEvent.VK_DOWN) && this.grounded == true) {
            this.crouching = true;
            this.state = State.CROUCHING;
        } else {
            this.crouching = false;
            this.state  = State.RUNNING;
        }
    }

    private void fall() {
        if (this.falling == true) {
            this.ySpeed += 1.5;
        }
    }

    private void move() {
        this.xPos += this.xSpeed;
        this.yPos += this.ySpeed;
    }

    private void collision () {

    }

    //input methods
    //-----------------------------------------------------------------------------------------------
    public static void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
    }

    //note: must use Integer type b/c e.getKeyCode returns type int
    //remove() is overloaded, and thinks that an argument of type int indicates the index of element to remove
    //while an argument of type Integer makes it actually find an element of that value
    public static void keyReleased(KeyEvent e) {
        keysPressed.remove(new Integer(e.getKeyCode()));
    }

    //drawing methods
    //-----------------------------------------------------------------------------------------------
    public void drawShape(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.red);

        AffineTransform at = AffineTransform.getTranslateInstance(this.xPos, this.yPos);

        switch (state) {
            case IDLE:
                Rectangle2D idle = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(idle));
            break;

            case WALKING:
                Rectangle2D walking = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(walking));
            break;

            case RUNNING:
                Rectangle2D running = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(running));
            break;

            case SLIDING:
                Rectangle2D sliding = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(sliding));
            break;

            case JUMPING:
                Rectangle2D jumping = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(jumping));
            break;

            case FALLING:
                Rectangle2D falling = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(falling));
            break;

            case LANDING:
                Rectangle2D landing = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(landing));
            break;

            case CROUCHING:
                Rectangle2D crouch = new Rectangle2D.Double(0D, 0D, this.width, this.height / 2);

                at.translate(0, this.height / 2);

                g2d.draw(at.createTransformedShape(crouch));
            break;

            case AT_WALL:
                Rectangle2D atWall = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(atWall));
            break;

        }


    }

    //debug methods
    //-----------------------------------------------------------------------------------------------
    public static String setToString(HashSet<Integer> setIn) {
        String stringOut = setIn.toString();

        return stringOut;
    }

    public String getXPos() {
        return Double.toString(this.xPos);
    }

    public String getYPos() {
        return Double.toString(this.yPos);
    }

    public String getXSpeed() {
        return Double.toString(this.xSpeed);
    }

    public String getYSpeed() {
        return Double.toString(this.ySpeed);
    }

    public String getState() {
        String stateOut;

        switch (this.state) {
            case IDLE:
                stateOut = "IDLE" ;
            break;

            case WALKING:
                stateOut = "WALKING";
            break;

            case RUNNING:
                stateOut = "RUNNING";
            break;

            case SLIDING:
                stateOut = "SLIDING";
            break;

            case JUMPING:
                stateOut = "JUMPING";
            break;

            case FALLING:
                stateOut = "FALLING";
            break;

            case LANDING:
                stateOut = "LANDING";
            break;
            
            case CROUCHING:
                stateOut = "CROUCHING";
            break;

            case AT_WALL:
                stateOut = "AT_WALL";
            break;

            default:
                stateOut = "";
            break;
        }
        return stateOut;
    }
}