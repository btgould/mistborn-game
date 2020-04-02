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

    private int xPos = 0;
    private int yPos = 190;
    private double xSpeed = 0;
    private double ySpeed = 0;

    private double width = 50;
    private double height = 100;

    //this set of variables is used to tweak the feel of the movement
    private double gravity = 1; //amount that ySpeed is changed for every frame the player falls
    private double airAcc = 0.5; //amount that xSpeed is changed for every frame the player moves in air
    private double walkAcc = 1; //amount that xSpeed is changed for every frame the player walks
    private double runAcc = 1; //amount that xSpeed is changed for every frame the player runs
    private double maxAirSpeed = 15; //max horiz airSpeed of player
    private double maxWalkSpeed = 8; //max speed the player can walk
    private double maxRunSpeed = 15; //max speed the player can run
    private double friction = 0.8; //proportion of speed that remains per frame while sliding
    private double fullJumpSpeed = -17; //initial ySpeed when the player jumps
    private double shortJumpSpeed = -10; //ySpeed to set if the player releases jump early
    

    private boolean canJump;

    // this set of bools is used to determine state.
    // they are not equivalent to state, because player can be accelerating and
    // falling, for example
    //TODO: change visibility back to private after debugging finished
    public boolean accelerating;
    public boolean canRun;
    public boolean sliding;

    public boolean grounded;
    public boolean jumping;
    public boolean falling;
    public boolean landing;

    public boolean crouching;
    public boolean atWall;
    public boolean wallPushing;

    private Side wallSide;
    private Side wallJumpSide;

    private State state;

    public static HashSet<Integer> keysPressed = new HashSet<>();

    public Player() {
        this.state = State.IDLE;
    }

    public void tick() {

        switch (state) {
            case IDLE:
                // assumptions from being in IDLE state
                this.grounded = true;
                this.crouching = false;
                this.accelerating = false;
                this.atWall = false;

                // check if assumptions were true
                checkForWalkAcc();
                checkForJump();
                checkForCrouch();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded and crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.atWall) {
                        // grounded, not crouching, at wall -> AT_WALL
                        this.state = State.AT_WALL;
                    } else if (this.accelerating) {
                        // grounded, not crouching, not at wall, accelerating -> WALKING
                        this.state = State.WALKING;
                    }
                } else {
                    // not grounded -> JUMPING
                    this.state = State.JUMPING;
                }
                break;

            case WALKING:
                // assumptions from being in WALKING state
                this.sliding = false;
                this.grounded = true;
                this.jumping = false;
                this.crouching = false;
                this.atWall = false;
                this.canRun = false;

                // check if assumptions were true
                checkForWalkAcc();
                checkForRun();
                checkForJump();
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                move();
                checkForCollision();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded and crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.atWall) {
                        // grounded, not crouching, at wall -> AT_WALL
                        this.state = State.AT_WALL;
                    } else if (this.sliding) {
                        // grounded, not crouching, not at wall, sliding -> SLIDING
                        this.state = State.SLIDING;
                    } else if (Math.abs(this.xSpeed) >= this.maxWalkSpeed && this.canRun) {
                        // grounded, not crouching, not at wall, not sliding, speed >= 10 and canRun -> RUNNING
                        this.state = State.RUNNING;
                    }
                } else {
                    if (this.jumping) {
                        // not grounded, jumping -> JUMPING
                        this.state = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        this.state = State.FALLING;
                    }
                }
                break;

            case RUNNING:
                // assumptions from being in RUNNING state
                this.sliding = false;
                this.grounded = true;
                this.jumping = false;
                this.crouching = false;
                this.atWall = false;
                this.canRun = true;

                // check if assumptions were true
                checkForRunAcc();
                checkForRun();
                checkForJump();
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                move();
                checkForCollision();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded and crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.atWall) {
                        // grounded, not crouching, at wall -> AT_WALL
                        this.state = State.AT_WALL;
                    } else if (this.sliding) {
                        // grounded, not crouching, not at wall, sliding -> SLIDING
                        this.state = State.SLIDING;
                    } else if (!this.canRun) {
                        this.state = State.WALKING;
                    }
                } else {
                    if (this.jumping) {
                        // not grounded, jumping -> JUMPING
                        this.state = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        this.state = State.FALLING;
                    }
                }
                break;

            case SLIDING:
                // assumptions from being in SLIDING state
                this.accelerating = false;
                this.grounded = true;
                this.jumping = false;
                this.crouching = false;
                this.atWall = false;

                // check if assumptions were true
                checkForWalkAcc();
                checkForJump();
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                move();
                checkForCollision();

                if (this.grounded) {
                    if (this.crouching) {
                        // grounded and crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.atWall) {
                        // grounded, not crouching, at wall -> AT_WALL
                        this.state = State.AT_WALL;
                    } else if (this.accelerating) {
                        // grounded, not crouching, not at wall, accelerating -> determine based on
                        // speed & run ability
                        if (Math.abs(this.xSpeed) >= this.maxWalkSpeed && this.canRun) {
                            this.state = State.RUNNING;
                        } else if (Math.abs(this.xSpeed) > 0) {
                            this.state = State.WALKING;
                        }
                    } else if (this.xSpeed == 0) {
                        // grounded, not crouching, not at wall, not accelerating, speed = 0 -> IDLE
                        this.state = State.IDLE;
                    }
                } else {
                    if (this.jumping) {
                        // not grounded, jumping -> JUMPING
                        this.state = State.JUMPING;
                    } else {
                        // not grounded, not jumping -> FALLING
                        this.state = State.FALLING;
                    }
                }
                break;

            case JUMPING:

                checkForRun();
                checkForAirAcc();
                move();
                checkForCollision();
                checkIfAtWall();

                fall();

                /*
                 * if (keysPressed.contains(KeyEvent.VK_UP) && this.jumpHeightCount < 5) {
                 * this.ySpeed = -12; this.jumpHeightCount++; } else { this.state =
                 * State.FALLING; }
                 */

                if (!(keysPressed.contains(KeyEvent.VK_UP) && this.ySpeed < -10)) {
                    this.ySpeed = this.shortJumpSpeed;
                    this.state = State.FALLING;
                }
                break;

            case DOUBLE_JUMPING:
            
                break;

            case FALLING:
                // assumptions from being in FALLING state
                this.landing = false;

                fall();

                // check if assumptions were true
                checkForRun();
                checkForAirAcc();
                move();
                checkForCollision();
                checkIfAtWall();

                // determine next state
                if (this.wallPushing) {
                    this.state = State.WALL_FALLING;
                } else if (this.landing) {
                    this.state = State.LANDING;
                }
                break;

            case WALL_FALLING:
                // assumptions from being in WALL_FALLING state
                this.landing = false;

                fall();

                // check if assumptions were true
                checkForRun();
                checkForAirAcc();
                move();
                checkForCollision();
                checkIfAtWall();

                // determine next state
                if (this.landing) {
                    this.state = State.LANDING;
                } else  if (!this.wallPushing) {
                    this.state = State.FALLING;
                }

                break;

            case WALL_JUMPING:
            
                break;

            // TODO: make landing state last for more than one frame
            case LANDING:
                // assumptions from being in LANDING state
                this.sliding = false;

                // check if assumptions were true
                checkForWalkAcc();
                checkForFriction();
                move();
                checkForCollision();

                // determine next state
                if (this.sliding) {
                    // sliding (triggered by not pressing left or right) -> SLIDING
                    this.state = State.SLIDING;
                } else if (this.xSpeed >= this.maxWalkSpeed && this.canRun) {
                    // not sliding -> determine based on xSpeed & run ability
                    this.state = State.RUNNING;
                } else if (Math.abs(this.xSpeed) > 0) {
                    this.state = State.WALKING;
                } else {
                    this.state = State.IDLE;
                }
                break;

            // TODO: actually change height when crouching, force crouch when in too small
            // of a space
            case CROUCHING:
                // assumptions from being in CROUCHING state
                this.accelerating = false;
                this.grounded = true;
                this.crouching = true;
                this.atWall = false;

                // check if assumptions were true
                checkForCrouch();
                checkForWalkAcc();
                checkForFriction();
                checkIfFalling();
                move();
                checkForCollision();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded, crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.atWall) {
                        // grounded, not crouching, at wall -> AT_WALL
                        this.state = State.AT_WALL;
                    } else {
                        if (this.accelerating) {
                            // grounded, not at a wall, not crouching, and accelerating -> set based on
                            // speed & run ability
                            if (Math.abs(this.xSpeed) >= this.maxWalkSpeed && this.canRun) {
                                this.state = State.RUNNING;
                            } else if (Math.abs(this.xSpeed) > 0) {
                                this.state = State.WALKING;
                            }
                        } else if (this.xSpeed == 0) {
                            // grounded, not at wall, not crouching, not accelerating, speed = 0 -> IDLE
                            this.state = State.IDLE;
                        } else {
                            // grounded, not at wall, not crouching, not accelerating, speed != 0 -> SLIDING
                            this.state = State.SLIDING;
                        }
                    }
                } else {
                    // not grounded -> FALLING (b/c cant jump)
                    this.state = State.FALLING;
                }
                break;

            case AT_WALL:
                // assumptions from being in AT_WALL state
                this.atWall = true;
                this.grounded = true;
                this.accelerating = false;
                this.crouching = false;

                // check if assumptions are true
                checkForWalkAcc();
                checkForJump();
                checkForCrouch();
                checkForFriction();
                checkIfFalling();
                move();
                checkForCollision();
                checkIfAtWall();

                // determine next state
                if (this.grounded) {
                    if (this.crouching) {
                        // grounded, crouching -> CROUCHING
                        this.state = State.CROUCHING;
                    } else if (this.accelerating) {
                        // grounded, not crouching, accelerating (can only accelerate away from wall) ->
                        // WALKING
                        this.state = State.WALKING;
                    } else if (!this.wallPushing) {
                        // grounded, not crouching, not accelerating, not wall pushing -> IDLE
                        this.state = State.IDLE;
                    }
                } else {
                    // not grounded -> JUMPING
                    this.state = State.JUMPING;
                }

                break;
            
        }
    }

    // movement methods
    // -----------------------------------------------------------------------------------------------
    private void checkForWalkAcc() {
        this.wallPushing = false;

        // speed up to the left
        if (keysPressed.contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * maxWalkSpeed && !this.crouching) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= this.walkAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (keysPressed.contains(KeyEvent.VK_RIGHT) && this.xSpeed < maxWalkSpeed && !this.crouching) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += this.walkAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        if (Math.abs(this.xSpeed) > this.maxWalkSpeed) {
            if (this.xSpeed > 0) {
                this.xSpeed--;
            } else {
                this.xSpeed++;
            }

            if (Math.abs(this.xSpeed) < this.maxWalkSpeed) {
                if (this.xSpeed > 0) {
                    this.xSpeed = this.maxWalkSpeed;
                } else {
                    this.xSpeed = -1 * this.maxWalkSpeed;
                }
            }
        }
    }

    private void checkForAirAcc() {
        this.wallPushing = false;

        if (this.canRun) {
            this.maxAirSpeed = this.maxRunSpeed;
        } else {
            this.maxAirSpeed = this.maxWalkSpeed;
        }

        // speed up to the left
        if (keysPressed.contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * maxAirSpeed) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= this.airAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (keysPressed.contains(KeyEvent.VK_RIGHT) && this.xSpeed < maxAirSpeed) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += this.airAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

    }

    private void checkForRunAcc() {
        this.wallPushing = false;

        // speed up to the left
        if (keysPressed.contains(KeyEvent.VK_LEFT) && this.xSpeed > -1 * maxRunSpeed && !this.crouching) {
            if (this.wallSide == Side.LEFT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed -= this.runAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }

        // speed up to the right
        if (keysPressed.contains(KeyEvent.VK_RIGHT) && this.xSpeed < maxRunSpeed && !this.crouching) {
            if (this.wallSide == Side.RIGHT) {
                this.atWall = true;
                this.wallPushing = true;
            } else {
                this.xSpeed += this.runAcc;
                this.atWall = false;
                this.accelerating = true;
                this.wallSide = Side.NONE;
            }
        }
    }

    private void checkForRun() {
        if (keysPressed.contains(KeyEvent.VK_SHIFT)) {
            this.canRun = true;
        } else {
            this.canRun = false;
        }
    }

    private void checkForFriction() {
        if ((!(keysPressed.contains(KeyEvent.VK_RIGHT)) && !(keysPressed.contains(KeyEvent.VK_LEFT)))
                || this.state == State.CROUCHING) {
            this.xSpeed *= this.friction;

            this.sliding = true;

            if (Math.abs(this.xSpeed) <= 1) {
                this.xSpeed = 0;
                this.sliding = false;
            }
        }
    }

    private void checkForJump() {
        // jump
        if (keysPressed.contains(KeyEvent.VK_UP) && this.canJump == true) {
            this.ySpeed = this.fullJumpSpeed;

            this.canJump = false;
            this.grounded = false;
            this.falling = true;
            this.jumping = true;
        }
    }

    private void checkForCrouch() {
        if (keysPressed.contains(KeyEvent.VK_DOWN) && this.grounded == true) {
            this.crouching = true;
        } else {
            this.crouching = false;
        }
    }

    private void checkIfFalling() {
        this.yPos++;
        if (!collided()) {
            this.falling = true;
            this.canJump = false;
            this.grounded = false;
        }

        this.yPos--;
    }

    private void checkIfAtWall() {
        int side = 0;

        if (this.wallSide == Side.RIGHT) {
            this.xPos++;
            side = 1;
        } else if (this.wallSide == Side.LEFT) {
            this.xPos--;
            side = -1;
        }

        if (!collided()) {
            this.atWall = false;
            this.wallSide = Side.NONE;
        }

        if (side == 1) {
            this.xPos--;
        } else if (side == -1) {
            this.xPos++;
        }
    }

    //returns true if overlapping with a platform
    public boolean collided() {
        //check every platform for collision
        for (int platNum = 0; platNum < Platform.platforms.size(); platNum++) {
            Platform platform  = Platform.platforms.get(platNum);
            boolean onSameX = false;
            boolean onSameY = false;

            if (this.xPos + this.width  + 2 > platform.xPos && platform.xPos + platform.width  + 2 > this.xPos) {
                onSameX = true;
            }

            if (this.yPos + this.height  + 2 > platform.yPos && platform.yPos + platform.height  + 2 > this.yPos) {
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

    //TODO: colliding with roof works weirdly
    private void checkForCollision() {
        if (collided()) {
            double slope  = Math.abs(this.ySpeed / this.xSpeed);

            double xOffset = 0;
            double yOffset = 0;

            //xOffset < Math.abs(this.xSpeed) || yOffset < Math.abs(this.ySpeed)
            while (collided()) {
                if (xOffset == 0 && yOffset == 0) {
                    if (Math.abs(this.ySpeed) > Math.abs(this.xSpeed)) {
                        //tick y
                        if (this.ySpeed > 0) {
                            this.yPos--;
                        } else if (this.ySpeed < 0) {
                            this.yPos++;
                        }
    
                        yOffset++;
        
                        if (!collided()) {

                            if (this.ySpeed > 0) {
                                this.grounded = true;
                                this.canJump = true;
                                this.falling = false;
                                this.landing = true;
                            }
    
                            this.ySpeed = 0;
                            break;
                        }
                    } else {
                        //works b/c both cannot be 0, or player would never collide
                        //tick x
                        if (this.xSpeed > 0) {
                            this.xPos--;
                        } else if (this.xSpeed < 0) {
                            this.xPos++;
                        }
    
                        xOffset++;
        
                        if (!collided()) {
                            this.atWall = true;
    
                            if (this.xSpeed > 0) {
                                this.wallSide = Side.RIGHT;
                            } else if (this.xSpeed < 0) {
                                this.wallSide = Side.LEFT;
                            }
    
                            this.xSpeed = 0;
                            break;
                        }
                    }
                } else if (yOffset / xOffset >= slope) {
                    //tick x
                    if (this.xSpeed > 0) {
                        this.xPos--;
                    } else if (this.xSpeed < 0) {
                        this.xPos++;
                    }

                    xOffset++;
    
                    if (!collided()) {
                        this.atWall = true;

                        if (this.xSpeed > 0) {
                            this.wallSide = Side.RIGHT;
                        } else if (this.xSpeed < 0) {
                            this.wallSide = Side.LEFT;
                        }

                        this.xSpeed = 0;
                        break;
                    }
                } else {
                    //tick y
                    if (this.ySpeed > 0) {
                        this.yPos--;
                    } else if (this.ySpeed < 0) {
                        this.yPos++;
                    }

                    yOffset++;
    
                    if (!collided()) {

                        if (this.ySpeed > 0) {
                            this.grounded = true;
                            this.canJump = true;
                            this.falling = false;
                            this.landing = true;
                        }

                        this.ySpeed = 0;
                        break;
                    }
                }
            }
        }
    }
    
    private void fall() {
        if (this.falling == true) {
            this.ySpeed += this.gravity;
        }
    }

    private void move() {
        this.xPos += this.xSpeed;
        this.yPos += this.ySpeed;
    }

    // input methods
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

            case DOUBLE_JUMPING:
                //TODO: add drawing methods for new cases (double jumping, wall falling, wall jumping)
            break;

            case FALLING:
                Rectangle2D falling = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(falling));
            break;

            case WALL_FALLING:
                Rectangle2D wallFalling = new Rectangle2D.Double(0D, 0D, this.width, this.height);

                g2d.draw(at.createTransformedShape(wallFalling));
            break;

            case WALL_JUMPING:

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

        return this.state.toString();
    }
}