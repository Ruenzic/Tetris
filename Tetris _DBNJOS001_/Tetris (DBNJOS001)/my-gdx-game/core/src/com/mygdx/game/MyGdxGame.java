package com.mygdx.game;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class MyGdxGame extends ApplicationAdapter {
	long startTime = 0;
	float gravityTime = 0;
	double limit = 0.6;
	double drop = 0;
	double limitTemp = 0.6;
	float horTime = 0;
	float verTime = 0;
	SpriteBatch batch;
	Random rand = new Random();
	int selected = 0;
	int rowCount = 0;
	float timeCheck = 0;
	boolean special = false;
	float specialCount = 0;
	boolean specialAvailible = true;
	boolean status = false;
	boolean isDrop = false;
	int Level = 1;
	
	int Score = 0;
	String ScoreName = "Score: 0";
	BitmapFont scoreText;
	Scanner scan;
	String Highscore;
	
	Music mp3Sound;
	Music mp3Sound2;
	Music mp3Sound3;
	Sound move;
	Sound lock;
	
	Button Startbutton; 
	
	boolean isVertical = true;
	boolean isHorizontal = false;
	boolean isRight = true;
	boolean isLeft = true;
	int Pyramid = 1;
	int L = 1;
	int OPL = 1;
	
	boolean LmoveNow = false;
	boolean LmoveOld = false;
	boolean RmoveNow = false;
	boolean RmoveOld = false;
	boolean UmoveNow = false;
	boolean UmoveOld = false;
	boolean restartNow = false;
	boolean restartOld = false;
	boolean spaceNow = false;
	boolean spaceOld = false;
	boolean VNow = false;
	boolean VOld = false;

	Texture cursor;
	float cursorX = 160;
	float cursorY = 420;
	
	float cursorA = 160 - 20;
	float cursorB = 420;
	
	float cursorC = 160 - 20;
	float cursorD = 420 + 20;
	
	float cursorE = 160 + 20;
	float cursorF = 420;
	
	Texture block;
	float blockW = 20;
	float blockH = 20;
	
	Texture blockB;
	Texture blockBG;
	Texture blockP;
	Texture green;
	Texture orange;
	Texture purp;
	Texture red;
	Texture turc;
	Texture yellow;
	Texture blue;
	
	String colour = "blue";
	
	int gridW = 20;
	int gridH = 30; // 30
	
	int[][] grid = new int[gridW][gridH];
	boolean[][]check = new boolean[gridW][gridH];
	
	@Override
	public void create () { // create all the objects for block textures and music and sounds. 
		batch = new SpriteBatch();
		cursor = new Texture("cursorNew.png");  
		block = new Texture("block.png");
		blockB = new Texture("border.png");
		blockBG = new Texture("green.png");
		blockP = new Texture("place.png");
		scoreText = new BitmapFont();
		
		green = new Texture("greenNew.png");
		orange = new Texture("orangeNew.png");
		purp = new Texture("purpNew.png");
		red = new Texture("redNew.png");
		turc = new Texture("turcNew.png");
		yellow = new Texture("yellowNew.png");
		blue = new Texture("blueNew.png");
		
		try {
			getScore();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		move = Gdx.audio.newSound(Gdx.files.internal("beep-07.wav"));
		lock = Gdx.audio.newSound(Gdx.files.internal("beep-08b.wav"));
		mp3Sound = Gdx.audio.newMusic(Gdx.files.internal("244088__foolboymedia__generic-trance.mp3"));
		mp3Sound.setLooping(false);
		//mp3Sound.play();
		
		mp3Sound2 = Gdx.audio.newMusic(Gdx.files.internal("Royalty Free Music - Alex Badea - Silence.mp3"));
		mp3Sound2.setLooping(true);
		mp3Sound2.setVolume((float) 0.35);
		mp3Sound2.play();
		
	}
	
	 @Override
    public void dispose() {
        batch.dispose();
        cursor.dispose();
		block.dispose();
		blockB.dispose();
		blockBG.dispose();
		mp3Sound.dispose();
		mp3Sound2.dispose();
		blockP.dispose();
		scoreText.dispose();
		green.dispose();
		orange.dispose();
		purp.dispose();
		red.dispose();
		turc.dispose();
		yellow.dispose();
		blue.dispose();
		move.dispose();
		lock.dispose();
		
    }

	@Override
	public void render () {
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) { // instant drop
			spaceNow = Gdx.input.isKeyPressed(Input.Keys.SPACE);
			if (spaceNow && !spaceOld)
        	{
				drop = limit;
				limit = 0;
				isDrop = true;
        	}
			spaceOld = spaceNow;
			
        }
		spaceOld = Gdx.input.isKeyPressed(Input.Keys.SPACE);
		
		
		if(Gdx.input.isKeyPressed(Input.Keys.V)) { // use special ability
			VNow = Gdx.input.isKeyPressed(Input.Keys.V);
			if (VNow && !VOld)
        	{
				if (specialAvailible == true)
				{
					special = true;
					limitTemp = limit;
					limit = 1.5;
					timeCheck = 0;
					specialAvailible = false;
				}
        	}
			VOld = VNow;
			
        }
		VOld = Gdx.input.isKeyPressed(Input.Keys.V);
		
		if(Gdx.input.isKeyPressed(Input.Keys.R)) { // restart the game
			restartNow = Gdx.input.isKeyPressed(Input.Keys.R);
			if (restartNow && !restartOld)
        	{
				restart();
        	}
			restartOld = restartNow;
        }
		restartOld = Gdx.input.isKeyPressed(Input.Keys.R);
		
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) { // rotate block
        	UmoveNow = Gdx.input.isKeyPressed(Input.Keys.UP);
        	if (UmoveNow && !UmoveOld)
        	{
        		rotate();
        		lock.setVolume(move.play(), (float) 0.2);
        	}
        	UmoveOld = UmoveNow;
        }
        UmoveOld = Gdx.input.isKeyPressed(Input.Keys.UP);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){ // move down
        if ((int) (cursorY/20)-1 < 1 || (int) (cursorB/20)-1 < 1 || (int) (cursorD/20)-1 < 1 || (int) (cursorF/20)-1 < 1)
        {
        	cursorY = cursorY - 0;
        	check[(int) (cursorX/20)][(int) (cursorY/20)] = true;
    		check[(int) (cursorA/20)][(int) (cursorB/20)] = true;
    		check[(int) (cursorC/20)][(int) (cursorD/20)] = true;
    		check[(int) (cursorE/20)][(int) (cursorF/20)] = true;
    		if (!status)
    		{
    			lock.setVolume(lock.play(), (float) 0.2);
    			newShape();
    		}
        }
        else if (check[(int) cursorX/20][(int) (cursorY/20)-1] == true || check[(int) cursorA/20][(int) (cursorB/20)-1] == true || check[(int) cursorC/20][(int) (cursorD/20)-1] == true || check[(int) cursorE/20][(int) (cursorF/20)-1] == true)
        {
        	cursorY = cursorY - 0;
        	check[(int) (cursorX/20)][(int) (cursorY/20)] = true;
    		check[(int) (cursorA/20)][(int) (cursorB/20)] = true;
    		check[(int) (cursorC/20)][(int) (cursorD/20)] = true;
    		check[(int) (cursorE/20)][(int) (cursorF/20)] = true;
    		if (!status)
    		{
    			lock.setVolume(lock.play(), (float) 0.2);
    			newShape();
    		}
        	
        }
        else if((int) cursorY/20 <= 0 || (int) cursorB/20 <= 0 || (int) cursorD/20 <= 0 || (int) cursorF/20 <= 0)
        	{
        		cursorY = cursorY - 0;
        		check[(int) (cursorX/20)][(int) (cursorY/20)] = true;
        		check[(int) (cursorA/20)][(int) (cursorB/20)] = true;
        		check[(int) (cursorC/20)][(int) (cursorD/20)] = true;
        		check[(int) (cursorE/20)][(int) (cursorF/20)] = true;
        		if (!status)
        		{
        			lock.setVolume(lock.play(), (float) 0.2);
        			newShape();
        		}
        	}
        	else
        	{
        		cursorY = cursorY - 7;
        		cursorB = cursorB - 7;
        		cursorD = cursorD - 7;
        		cursorF = cursorF - 7;
        	}
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) // move left
        {
        	LmoveNow = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        	if(LmoveNow && !LmoveOld)
        	{
	        	 if (check[(int) (cursorX/20)-1][(int) (cursorY/20)] == true || check[(int) (cursorA/20)-1][(int) (cursorB/20)] == true || check[(int) (cursorC/20)-1][(int) (cursorD/20)] == true || check[(int) (cursorE/20)-1][(int) (cursorF/20)] == true)
	 	        {
	 	        	cursorX = cursorX + 0;
	 	        }
	 	        else if (cursorX <= 20 || cursorA <= 20 || cursorC <= 20 || cursorE <= 20)
	 	          {
	 	            cursorX = cursorX - 0;
	 	          }
	 	        else
	 	        {
	 	        	cursorX = cursorX - 20;
	 	        	cursorA = cursorA - 20;
	 	        	cursorC = cursorC - 20;
	 	        	cursorE = cursorE - 20;
	 	        }
        	}
        	LmoveOld = LmoveNow;
        }
        LmoveOld = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){ // Move right
        	RmoveNow = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        	if(RmoveNow && !RmoveOld)
        	{
	        	if (check[(int) (cursorX/20)+1][(int) (cursorY/20)] == true || check[(int) (cursorA/20)+1][(int) (cursorB/20)] == true || check[(int) (cursorC/20)+1][(int) (cursorD/20)] == true || check[(int) (cursorE/20)+1][(int) (cursorF/20)] == true)
	        	{
	        		cursorX = cursorX + 0;
	        	}
	        	else if (cursorX >= 300 || cursorA >= 300 || cursorC >= 300 || cursorE >= 300)
	        	{
	        		cursorX = cursorX + 0;
	        	}
	        	else
	        	{
		        	cursorX = cursorX + 20;
		        	cursorA = cursorA + 20;
		        	cursorC = cursorC + 20;
		        	cursorE = cursorE + 20;
	        	}
        	}
        	RmoveOld = RmoveNow;
        }
        RmoveOld = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        
        
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		if (mp3Sound2.isPlaying() == false && mp3Sound.isPlaying() == false) // Check which song is playing and play next. 
		{
			mp3Sound.play();
		}
		if (specialAvailible == true) // Check and tell user if special ability is availible to use or not. 
		{
			scoreText.draw(batch, "SPECIAL AVAILIBLE", 360, 330);
		}
		else 
		{
			scoreText.draw(batch, "SPECIAL UNAVAILIBLE", 360, 330);
		}
		
		ScoreName = "Score: " + Score;
		scoreText.setColor(5.0f, 5.0f, 1.0f, 1.0f);
		scoreText.draw(batch, "TETRIS", 450, 465);
		scoreText.draw(batch, ScoreName, 360, 415);
		scoreText.draw(batch, Highscore, 360, 395);
		scoreText.draw(batch, "PRESS 'R' TO RESTART THE GAME", 360, 300);
		scoreText.draw(batch, "Controls:", 360, 250);
		scoreText.draw(batch, "Up-Arrow to Rotate block", 360, 230);
		scoreText.draw(batch, "Down-Arrow to increase speed", 360, 210);
		scoreText.draw(batch, "Left and Right arrows to move horizontally", 360, 190);
		scoreText.draw(batch, "Space instant drop", 360, 170);
		scoreText.draw(batch, "V to use Special Ability", 360, 150);
		scoreText.draw(batch, "TIP: Lose game to save highscore", 360, 100);
		scoreText.draw(batch, "Music: Silence by Alex Badea", 360, 50);
		scoreText.draw(batch, "           Generic Trance by FoolBoyMedia", 360, 30);
		if (status)
		{
			scoreText.draw(batch, "GAME OVER", 360, 360);
		}
		
		if (Level == 1)
		{
			scoreText.draw(batch, "LEVEL 1", 360, 360);
		}
		else if (Level == 2)
		{
			scoreText.draw(batch, "LEVEL 2", 360, 360);
		}
		else if (Level == 3)
		{
			scoreText.draw(batch, "LEVEL 3", 360, 360);
		}
		else if (Level == 4)
		{
			scoreText.draw(batch, "LEVEL 4", 360, 360);
		}
		else if (Level == 5)
		{
			scoreText.draw(batch, "LEVEL 5", 360, 360);
		}
		else if (Level == 6)
		{
			scoreText.draw(batch, "LEVEL 6", 360, 360);
		}
		else if (Level == 7)
		{
			scoreText.draw(batch, "LEVEL 7", 360, 360);
		}
		
		for(int j = 1; j < 23; j++) // draw the background
		{
			for(int x = 1; x < 16; x++)
			{
				batch.draw(blockBG, x * blockW, j * blockH);
			}
		}
		
		for(int i = 0; i < gridH; i++) {		// Draw the borders. 
			for(int l = 0; l < gridW; l++) {
				if(l == 0)
				{
					batch.draw(blockB, l * blockW, i * blockH); 
				}
				else if (l == 16)
				{
					batch.draw(blockB, l * blockW, i * blockH);
				}
				else if (i == 0)
				{
					if (l <= 16)
					{
						batch.draw(blockB, l * blockW, i * blockH);
					}
				}
				else if (i == 23)
				{
					if (l <= 16)
					{
						batch.draw(blockB, l * blockW, i * blockH);
					}
				}
			}
		}
		
		//System.out.println((int) cursorX/20 + "," + (int) cursorY/20);
		if (!status)
		{
			specialCount += Gdx.graphics.getDeltaTime(); // Created a waiting period after you use special ability
			if (specialCount >= 30)
			{
				specialAvailible = true;
			}
			if (special == true)
			{
				timeCheck += Gdx.graphics.getDeltaTime();
				
				if (timeCheck > 5)
				{
					limit = limitTemp;
					timeCheck = timeCheck -5;
					special = false;
					specialCount = 0;
				}
			}
			else
			{
				gravityTime += Gdx.graphics.getDeltaTime();
				if (gravityTime > limit)
				{
					gravity();
					gravityTime -= limit;
				}
			}
		}
		
		for(int y = 0; y < gridH; y++) {		
			for(int x = 0; x < gridW; x++) {
				if(check[x][y] == true)
				{
					batch.draw(blockP, x * blockW, y * blockH); // Draw all the blocks that have been placed. 
					checkRow(x, y);
					if(y >= 22)
					{
						setScore();
						status = true;
						Level = 0;
						limit = 0;
					}
				}
			}
	}
		
		if (!status)
		{
			drawBlock();
		}
		
		batch.end();
	}
	

	@Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
    
    public void startTime(){
		startTime = System.currentTimeMillis();
    }
    
    public float endTime(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
    
    public void gravity() // method to create gravity
    {
    	
    	if ((int) (cursorY/20)-1 < 1 || (int) (cursorB/20)-1 < 1 || (int) (cursorD/20)-1 < 1 || (int) (cursorF/20)-1 < 1)
        {
        	cursorY = cursorY - 0;
    		check[(int) (cursorX/20)][(int) (cursorY/20)] = true;
    		check[(int) (cursorA/20)][(int) (cursorB/20)] = true;
    		check[(int) (cursorC/20)][(int) (cursorD/20)] = true;
    		check[(int) (cursorE/20)][(int) (cursorF/20)] = true;
    		cursorX = 160;
    		cursorY = 420;
    		if (!status)
    		{
    			isDrop = false;
    			limit = drop;
    			lock.setVolume(lock.play(), (float) 0.2);
    			newShape();
    		}
        }
        else if (check[(int) cursorX/20][(int) (cursorY/20)-1] == true || check[(int) cursorA/20][(int) (cursorB/20)-1] == true || check[(int) cursorC/20][(int) (cursorD/20)-1] == true || check[(int) cursorE/20][(int) (cursorF/20)-1] == true)
        {
        	cursorY = cursorY - 0;
        	check[(int) (cursorX/20)][(int) (cursorY/20)] = true;
    		check[(int) (cursorA/20)][(int) (cursorB/20)] = true;
    		check[(int) (cursorC/20)][(int) (cursorD/20)] = true;
    		check[(int) (cursorE/20)][(int) (cursorF/20)] = true;
    		if (!status)
    		{
    			isDrop = false;
    			limit = drop;
    			lock.setVolume(lock.play(), (float) 0.2);
    			newShape();
    		}
        	
        }
        else if((int) cursorY/20 <= 0 || (int) cursorB/20 <= 0 || (int) cursorD/20 <= 0 || (int) cursorF/20 <= 0)
        	{
        		cursorY = cursorY - 0;
        		check[(int) (cursorX/20)][(int) (cursorY/20)] = true;
        		check[(int) (cursorA/20)][(int) (cursorB/20)] = true;
        		check[(int) (cursorC/20)][(int) (cursorD/20)] = true;
        		check[(int) (cursorE/20)][(int) (cursorF/20)] = true;
        		if (!status)
        		{
        			isDrop = false;
        			limit = drop;
        			lock.setVolume(lock.play(), (float) 0.2);
        			newShape();
        		}
        	}
    	else
    	{
    		cursorY = cursorY - 20;
    		cursorB = cursorB - 20;
    		cursorD = cursorD - 20;
    		cursorF = cursorF - 20;
    	}
    }
    
    
    
    public void newShape() // create a new shape
    {
    	selected = rand.nextInt(7); // this will give us a value between 0 and 6, to choose which of the 7 blocks will spawn next. 
    	if (selected == 0) // Opposite L
    	{
    		colour = "blue";
    		cursorX = 160;
    		cursorY = 420;
        	cursorA = cursorX - 20;
        	cursorB = cursorY;
        	cursorC = cursorX - 20;
        	cursorD = cursorY + 20;
        	cursorE = cursorX + 20;
        	cursorF = cursorY;
        	OPL = 1;
    	}
    	if (selected == 1) // L
    	{
    		colour = "green";
    		cursorX = 160;
    		cursorY = 420;
        	cursorA = cursorX - 20;
        	cursorB = cursorY;
        	cursorC = cursorX + 20;
        	cursorD = cursorY + 20;
        	cursorE = cursorX + 20;
        	cursorF = cursorY;
        	L = 1;
    	}
    	if (selected == 2) // Straight line
    	{
    		colour = "orange";
    		cursorX = 160;
    		cursorY = 420;
        	cursorA = cursorX;
        	cursorB = cursorY + 20;
        	cursorC = cursorX;
        	cursorD = cursorY - 20;
        	cursorE = cursorX;
        	cursorF = cursorY - 40;
        	isVertical = true;
    	}
    	if (selected == 3) // Square
    	{
    		colour = "purp";
    		cursorX = 160;
    		cursorY = 420;
        	cursorA = cursorX;
        	cursorB = cursorY + 20;
        	cursorC = cursorX + 20;
        	cursorD = cursorY;
        	cursorE = cursorX + 20;
        	cursorF = cursorY + 20;
    	}
    	if (selected == 4) // Z
    	{
    		colour = "red";
    		cursorX = 160;
    		cursorY = 420;
        	cursorA = cursorX - 20;
        	cursorB = cursorY;
        	cursorC = cursorX;
        	cursorD = cursorY + 20;
        	cursorE = cursorX + 20;
        	cursorF = cursorY + 20;
        	isRight = true;
    	}
    	if (selected == 5) // Opposite Z
    	{
    		colour = "turc";
    		cursorX = 160;
    		cursorY = 420;
        	cursorA = cursorX + 20;
        	cursorB = cursorY;
        	cursorC = cursorX;
        	cursorD = cursorY + 20;
        	cursorE = cursorX - 20;
        	cursorF = cursorY + 20;
        	isLeft = true;
    	}
    	if (selected == 6) // Pyramid
    	{
    		colour = "yellow";
    		cursorX = 160;
    		cursorY = 420;
        	cursorA = cursorX - 20;
        	cursorB = cursorY;
        	cursorC = cursorX;
        	cursorD = cursorY + 20;
        	cursorE = cursorX + 20;
        	cursorF = cursorY;
        	Pyramid = 1;
    	}
    	
    	
    }
    
    public void rotate() // rotate the current block
    {
    	if(cursorX/20 >= 2 && cursorX/20 <= 14 && cursorY/20 >= 2)
    	{
	    	if (selected == 0) // Opposite L
	    	{
	    		if (OPL == 1)
	    		{
	    			cursorA = cursorX;
	    			cursorB = cursorY + 20;
	    			cursorC = cursorX + 20;
	    			cursorD = cursorY + 20;
	    			cursorE = cursorX;
	    			cursorF = cursorY - 20;
	    			OPL = 2;
	    		}
	    		else if (OPL == 2)
	    		{
	    			cursorA = cursorX + 20;
	    			cursorB = cursorY;
	    			cursorC = cursorX + 20;
	    			cursorD = cursorY - 20;
	    			cursorE = cursorX - 20;
	    			cursorF = cursorY;
	    			OPL = 3;
	    		}
	    		else if (OPL == 3)
	    		{
	    			cursorA = cursorX;
	    			cursorB = cursorY - 20;
	    			cursorC = cursorX - 20;
	    			cursorD = cursorY - 20;
	    			cursorE = cursorX;
	    			cursorF = cursorY + 20;
	    			OPL = 4;
	    		}
	    		else if (OPL == 4)
	    		{
	    			cursorA = cursorX - 20;
	    			cursorB = cursorY;
	    			cursorC = cursorX - 20;
	    			cursorD = cursorY + 20;
	    			cursorE = cursorX + 20;
	    			cursorF = cursorY;
	    			OPL = 1;
	    		}
	    	}
    	}
    	if (selected == 1) // L
    	{
    		if(cursorX/20 >= 2 && cursorX/20 <= 14 && cursorY/20 >= 2)
        	{
	    		if (L == 1)
	    		{
		        	cursorA = cursorX;
		        	cursorB = cursorY + 20;
		        	cursorC = cursorX + 20;
		        	cursorD = cursorY - 20;
		        	cursorE = cursorX;
		        	cursorF = cursorY - 20;
		        	L = 2;
	    		}
	    		else if (L == 2)
	    		{
	    			cursorA = cursorX + 20;
		        	cursorB = cursorY;
		        	cursorC = cursorX - 20;
		        	cursorD = cursorY - 20;
		        	cursorE = cursorX - 20;
		        	cursorF = cursorY;
		        	L = 3;
	    		}
	    		else if (L == 3)
	    		{
	    			cursorA = cursorX;
		        	cursorB = cursorY - 20;
		        	cursorC = cursorX - 20;
		        	cursorD = cursorY + 20;
		        	cursorE = cursorX;
		        	cursorF = cursorY + 20;
		        	L = 4;
	    		}
	    		else if (L == 4)
	    		{
	    			cursorA = cursorX - 20;
		        	cursorB = cursorY;
		        	cursorC = cursorX + 20;
		        	cursorD = cursorY + 20;
		        	cursorE = cursorX + 20;
		        	cursorF = cursorY;
		        	L = 1;
	    		}
	    	}
    	}
    	if (selected == 2) // Straight line
    	{
    		if(cursorX/20 >= 3 && cursorX/20 <= 14 && cursorY/20 >= 3)
        	{
	    		if (isVertical == true)
	    		{
		        	cursorA = cursorX - 20;
		        	cursorB = cursorY;
		        	cursorC = cursorX - 40;
		        	cursorD = cursorY;
		        	cursorE = cursorX + 20;
		        	cursorF = cursorY;
		        	isVertical = false;
		        	isHorizontal = true;
	    		}
	    		else if (isHorizontal == true)
	    		{
	    			
	    			cursorA = cursorX;
	            	cursorB = cursorY + 20;
	            	cursorC = cursorX;
	            	cursorD = cursorY - 20;
	            	cursorE = cursorX;
	            	cursorF = cursorY - 40;
	            	isVertical = true;
	            	isHorizontal = false;
	    		}
	    	}
    	}
    	
    	if (selected == 4) // Z
    	{
    		if(cursorX/20 >= 2 && cursorX/20 <= 14 && cursorY/20 >= 2)
        	{
	    		if(isRight == true)
	    		{
		        	cursorA = cursorX + 20;
		        	cursorB = cursorY;
		        	cursorC = cursorX;
		        	cursorD = cursorY + 20;
		        	cursorE = cursorX + 20;
		        	cursorF = cursorY - 20;
		        	isRight = false;
	    		}
	    		else if(isRight == false)
	    		{
	    			cursorA = cursorX - 20;
		        	cursorB = cursorY;
		        	cursorC = cursorX;
		        	cursorD = cursorY + 20;
		        	cursorE = cursorX + 20;
		        	cursorF = cursorY + 20;
		        	isRight = true;
	    		}
	    	}
    	}
    	if (selected == 5) //Opposite Z
    	{
    		if(cursorX/20 >= 2 && cursorX/20 <= 14 && cursorY/20 >= 2)
        	{
	    		if(isLeft == true)
	    		{
	    			cursorA = cursorX - 20;
	            	cursorB = cursorY;
	            	cursorC = cursorX;
	            	cursorD = cursorY + 20;
	            	cursorE = cursorX - 20;
	            	cursorF = cursorY - 20;
	            	isLeft = false;
	    		}
	    		else if(isLeft == false)
	    		{
	            	cursorA = cursorX + 20;
	            	cursorB = cursorY;
	            	cursorC = cursorX;
	            	cursorD = cursorY + 20;
	            	cursorE = cursorX - 20;
	            	cursorF = cursorY + 20;
	            	isLeft = true;
	    		}
	    	}
    	}
    	if (selected == 6) // Pyramid
    	{
    		if(cursorX/20 >= 2 && cursorX/20 <= 14 && cursorY/20 >= 2)
        	{
	    		if (Pyramid == 1)
	    		{
		        	cursorA = cursorX;
		        	cursorB = cursorY + 20;
		        	cursorC = cursorX + 20;
		        	cursorD = cursorY;
		        	cursorE = cursorX;
		        	cursorF = cursorY - 20;
		        	Pyramid = 2;
	    		}
	    		else if (Pyramid == 2)
	    		{
	    			cursorA = cursorX + 20;
		        	cursorB = cursorY;
		        	cursorC = cursorX;
		        	cursorD = cursorY - 20;
		        	cursorE = cursorX - 20;
		        	cursorF = cursorY;
		        	Pyramid = 3;
	    		}
	    		else if (Pyramid == 3)
	    		{
	    			cursorA = cursorX;
		        	cursorB = cursorY - 20;
		        	cursorC = cursorX - 20;
		        	cursorD = cursorY;
		        	cursorE = cursorX ;
		        	cursorF = cursorY + 20;
		        	Pyramid = 4;
	    		}
	    		else if(Pyramid == 4)
	    		{
	    			cursorA = cursorX - 20;
		        	cursorB = cursorY;
		        	cursorC = cursorX;
		        	cursorD = cursorY + 20;
		        	cursorE = cursorX + 20;
		        	cursorF = cursorY;
		        	Pyramid = 1;
	    		}
	    	}
    	}
    	
    }
    
    public void checkRow(int x, int y) // check if the whole row has been filled so it can be cleared. 
    {
    	for (int i = 1; i < 16; i ++)
    	{
    		if (check[i][y] == true)
    		{
    			rowCount++; // check if row is full
    		}
    	}
    	//System.out.println(rowCount + "!!!!!!!!!!!");
    	if (rowCount == 15)
    	{
    		for (int i = 1; i < 16; i ++)
    		{
    			check[i][y] = false; // remove the row
    		}
    		if (Score < 10)
    		{
    			Score++;
    		}
    		else if (Score >= 10 && Score < 20)
    		{
    			Score += 2;
    		}
    		else if (Score >= 20 && Score < 30)
    		{
    			Score += 3;
    		}
    		else if (Score >= 30 && Score < 50)
    		{
    			Score += 4;
    		}
    		else if (Score >= 50 && Score < 100)
    		{
    			Score += 5;
    		}
    		else if (Score >= 100)
    		{
    			Score += 10;
    		}
    		
    		
    		
    		for (int j = y; j < 26; j++) // move all above rows down.
    		{
    			for (int r = 1; r < 16; r++)
    			{
    				if (check[r][j] == true)
    				{
    					check[r][j] = false;
    					check[r][j-1] = true;
    				}
    			}
    		}
    		rowCount = 0; // keep at end.
    	}
    	else
    	{
    		rowCount = 0;
    	}
    	if (isDrop == false)
    	{
	    	if (Score >= 0  && Score < 5)
			{
				limit = 0.6;
				Level = 1;
			}
	    	else if (Score >= 5 && Score < 10)
			{
				limit = 0.5;
				Level = 2;
			}
			else if (Score >= 10 && Score < 20 )
			{
				limit = 0.43;
				Level = 3;
			}
			else if (Score >= 20 && Score < 30)
			{
				limit = 0.36;
				Level = 4;
			}
			else if (Score >= 30 && Score < 50)
			{
				limit = 0.3;
				Level = 4;
			}
			else if (Score >= 50 && Score < 100)
			{
				limit = 0.24;
				Level = 5;
			}
			else if (Score >= 100)
			{
				limit = 0.2;
				Level = 6;
			}
    	}
    }
    public void getScore() throws FileNotFoundException // get the current highscore
    {
    	boolean exists = Gdx.files.local("Highscore.txt").exists();
    	if(exists == true)
    	{
    		FileHandle scoreList = Gdx.files.local("Highscore.txt");
    		Highscore = "HighScore: " + scoreList.readString();
    	}
    	else
    	{
    		Highscore = "No Highscore";
    	}
    }
    
    public void setScore() // set the score (update it)
    {
    	FileHandle scoreList = Gdx.files.local("Highscore.txt");
    	if(Score > Integer.parseInt(scoreList.readString()))
    	scoreList.writeString("" + Score, false);
    }
    
    public void drawBlock() // draw the different blocks.
    {
    	if (colour.equals("blue"))
		{
			batch.draw(blue, cursorX, cursorY); // DRAW MAIN BLOCK.
			batch.draw(blue, cursorA, cursorB); // DRAW SURROUNDING BLOCKS HERE.
			batch.draw(blue, cursorC, cursorD);
			batch.draw(blue, cursorE, cursorF);
		}
		else if (colour.equals("green"))
		{
			batch.draw(green, cursorX, cursorY); // DRAW MAIN BLOCK.
			batch.draw(green, cursorA, cursorB); // DRAW SURROUNDING BLOCKS HERE.
			batch.draw(green, cursorC, cursorD);
			batch.draw(green, cursorE, cursorF);
		}
		else if (colour.equals("orange"))
		{
			batch.draw(orange, cursorX, cursorY); // DRAW MAIN BLOCK.
			batch.draw(orange, cursorA, cursorB); // DRAW SURROUNDING BLOCKS HERE.
			batch.draw(orange, cursorC, cursorD);
			batch.draw(orange, cursorE, cursorF);
		}
		else if (colour.equals("purp"))
		{
			batch.draw(purp, cursorX, cursorY); // DRAW MAIN BLOCK.
			batch.draw(purp, cursorA, cursorB); // DRAW SURROUNDING BLOCKS HERE.
			batch.draw(purp, cursorC, cursorD);
			batch.draw(purp, cursorE, cursorF);
		}
		else if (colour.equals("red"))
		{
			batch.draw(red, cursorX, cursorY); // DRAW MAIN BLOCK.
			batch.draw(red, cursorA, cursorB); // DRAW SURROUNDING BLOCKS HERE.
			batch.draw(red, cursorC, cursorD);
			batch.draw(red, cursorE, cursorF);
		}
		else if (colour.equals("turc"))
		{
			batch.draw(turc, cursorX, cursorY); // DRAW MAIN BLOCK.
			batch.draw(turc, cursorA, cursorB); // DRAW SURROUNDING BLOCKS HERE.
			batch.draw(turc, cursorC, cursorD);
			batch.draw(turc, cursorE, cursorF);
		}
		else if (colour.equals("yellow"))
		{
			batch.draw(yellow, cursorX, cursorY); // DRAW MAIN BLOCK.
			batch.draw(yellow, cursorA, cursorB); // DRAW SURROUNDING BLOCKS HERE.
			batch.draw(yellow, cursorC, cursorD);
			batch.draw(yellow, cursorE, cursorF);
		}
    	
    }
    
    public void restart() // Restart the game. 
    {
    	for(int y = 0; y < 26; y++) 
    	{		//gridH
			for(int x = 0; x < 16; x++)
			{   //gridW
				check[x][y] = false;
			}
    	}
    	newShape();
    	status = false;
    	limit = 0.6;
    	Score = 0;
    	Level = 1;
    	try {
			getScore();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
}
