/**
 * Represents a Crabby enemy in the game.
 */
package entities;

import static utilz.Constants.EnemyConstants.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import static utilz.Constants.Directions.*;
import main.Game;

public class Crabby extends Enemy {

	private int attackBoxOffsetX;

	/**
	 * Constructor for Crabby class.
	 * @param x Initial x-coordinate.
	 * @param y Initial y-coordinate.
	 */
	public Crabby(float x, float y) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
		initHitbox(22, 19);
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 30);
	}

	/**
	 * Updates Crabby's state based on game data and player position.
	 * @param lvlData Level data array.
	 * @param player The player object.
	 */
	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	private void updateBehavior(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData);
		else {
			switch (state) {
				case IDLE:
					newState(RUNNING);
					break;
				case RUNNING:
					if (canSeePlayer(lvlData, player)) {
						turnTowardsPlayer(player);
						if (isPlayerCloseForAttack(player))
							newState(ATTACK);
					}
					move(lvlData);
					break;
				case ATTACK:
					if (aniIndex == 0)
						attackChecked = false;
					if (aniIndex == 3 && !attackChecked)
						checkPlayerHit(attackBox, player);
					break;
				case HIT:
					break;
			}
		}
	}

	/**
	 * Flips the x-coordinate based on walking direction.
	 * @return Flipped x-coordinate.
	 */
	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	/**
	 * Flips the width based on walking direction.
	 * @return Flipped width.
	 */
	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}
}