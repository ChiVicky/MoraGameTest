package com.app17.moragame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app17.moragame.object.Computer;
import com.app17.moragame.object.Player;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Computer.OnComputerCompletedListener {
    //logt
    private static final String TAG = "MainActivity";
    public static final int EVEN = 0;
    public static final int PLAYER_WIN = 1;
    public static final int COMPUTER_WIN = 2;
    private ImageButton scissorsBtn, rockBtn, paperBtn;
    private Button startBtn, quitBtn;
    private TextView countText, winCountText, roundText, hartText;
    private ImageView computerImage, playerImage;
    private Player player;
    private Computer computer;
    private boolean isPlayerRound;
    private Timer timer;
    private long gameRoundSeconds = 1500;
    private int gameRound;
    private boolean isWin;
    private int score;
    private boolean gameOver = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        playerImage.setVisibility(View.INVISIBLE);

        computer = new Computer();
        computer.setOnComputerCompletedListener(this);

        timer = new Timer(3000, true, new Timer.OnTimerListener() {
            @Override
            public void onTick(long milliseconds) {
                Log.d(TAG, "run1: " + milliseconds);
            }

            @Override
            public void onTime(long milliseconds) {
                Log.d(TAG, "time's up1: " + milliseconds);
            }
        });

        timer.setStepMilliseconds(200);
        timer.start();
    }

    private void findViews() {
        scissorsBtn = findViewById(R.id.scissors_ibn);
        rockBtn = findViewById(R.id.rock_ibn);
        paperBtn = findViewById(R.id.paper_ibn);
        startBtn = findViewById(R.id.start_btn);
        quitBtn = findViewById(R.id.quit_btn);
        countText = findViewById(R.id.counter_text);

        computerImage = findViewById(R.id.computer_img);
        playerImage = findViewById(R.id.player_img);
        winCountText = findViewById(R.id.win_count_text);
        roundText = findViewById(R.id.round_text);
        hartText = findViewById(R.id.hart_text);

        View[] views = {scissorsBtn, rockBtn, paperBtn, startBtn, quitBtn};
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    public void initGame() {
        player = new Player();
        gameRound = 0;
        gameRoundSeconds = 1500;
        isWin = false;
        score = 0;
        gameOver = false;
        roundText.setText(getResources().getString(R.string.round) + " " + (gameRound + 1));
        winCountText.setText(String.format("%03d", score));
        String hart = " ";
        for (int i = 0; i < player.getLife(); i++) {
            hart += "♥";
        }
        hartText.setText(hart);
        if (timer != null) {
            timer.setAlive(false);
        }


        timer = new Timer(5000, true, new Timer.OnTimerListener() {
            @Override
            public void onTick(long milliseconds) {
                sendTimerMessage(milliseconds, 3);
            }

            @Override
            public void onTime(long milliseconds) {
                sendTimerMessage(milliseconds, 4);
            }
        });


        timer.setStepMilliseconds(50);
        timer.start();

        isPlayerRound = false;
        playerImage.setVisibility(View.INVISIBLE);
        computer.ai();
    }

    public void sendTimerMessage(long milliseconds, int what) {
        int seconds = (int) milliseconds / 1000;
        milliseconds = milliseconds % 1000;

        Message message = new Message();
        message.what = what;
        message.obj = String.format("%d:%03d", seconds, milliseconds);
        handler.sendMessage(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scissors_ibn:
                if (isPlayerRound) {
                    isPlayerRound = false;
                    player.setMora(Player.SCISSOR);
                    playerImage.setImageResource(R.drawable.scissors);
                    playerImage.setVisibility(View.VISIBLE);
                    checkGameState();
                }

                Log.d(TAG, "onClick: " + getResources().getString(R.string.scissors));
                break;
            case R.id.rock_ibn:
                if (isPlayerRound) {
                    isPlayerRound = false;
                    player.setMora(Player.ROCK);
                    playerImage.setImageResource(R.drawable.rock);
                    playerImage.setVisibility(View.VISIBLE);
                    checkGameState();
                }

                Log.d(TAG, "onClick: " + getResources().getString(R.string.rock));
                break;
            case R.id.paper_ibn:
                if (isPlayerRound) {
                    isPlayerRound = false;
                    player.setMora(Player.PAPER);
                    playerImage.setImageResource(R.drawable.paper);
                    playerImage.setVisibility(View.VISIBLE);
                    checkGameState();
                }

                Log.d(TAG, "onClick: " + getResources().getString(R.string.paper));
                break;
            case R.id.start_btn:
                if (gameOver) {
                    initGame();
                }
                //Toast.makeText(this, getResources().getString(R.string.start), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: " + getResources().getString(R.string.start));
                break;
            case R.id.quit_btn:
                if ((timer != null) || gameOver) {
                    timer.setAlive(false);
                    gameOver = true;
                }
//                this.finish();
//                System.exit(0);
                //Toast.makeText(this, getResources().getString(R.string.quit), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: " + getResources().getString(R.string.quit));
                break;
        }
    }

    @Override
    public void complete() {
        int mora = computer.getMora();
        handler.sendEmptyMessage(mora);

        Log.d(TAG, "complete: " + mora);

    }


    public int getWinState(int playerMora, int computerMora) {

        if (playerMora == Player.READY) {
            return COMPUTER_WIN;
        }

        if (playerMora == computerMora) {
            return EVEN;
        }

        if (playerMora == Player.PAPER && computerMora == Player.SCISSOR) {
            return COMPUTER_WIN;
        }

        if (playerMora == Player.SCISSOR && computerMora == Player.PAPER) {
            return PLAYER_WIN;
        }

        if (playerMora > computerMora) {
            return PLAYER_WIN;
        }

        return COMPUTER_WIN;

    }

    private void checkGameState() {
        int state = getWinState(player.getMora(), computer.getMora());
        if (state == EVEN) {
            Log.d(TAG, "checkGameState: " + "平手!");
        } else if (state == PLAYER_WIN) {
            isWin = true;
            score += 1;
            winCountText.setText(String.format("%03d", score));
            Log.d(TAG, "checkGameState: " + "玩家勝!");
        } else if (state == COMPUTER_WIN) {
            player.setLife(player.getLife() - 1);
            String hart = " ";
            for (int i = 0; i < player.getLife(); i++) {
                hart += "♥";
            }
            hartText.setText(hart);
            Log.d(TAG, "hart: " + hart);
            Log.d(TAG, "checkGameState: " + "電腦勝!");
            if (player.getLife() == 0) {
                gameOver = true;
                return;
            }
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nextRound();
//                initGame();
            }
        }).start();
    }


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    computerImage.setImageResource(R.drawable.scissors);
                    isPlayerRound = true;
                    break;
                case 1:
                    computerImage.setImageResource(R.drawable.rock);
                    isPlayerRound = true;
                    break;
                case 2:
                    computerImage.setImageResource(R.drawable.paper);
                    isPlayerRound = true;
                    break;
                case 3:
                    countText.setText((String) msg.obj);
                    break;
                case 4:
                    isPlayerRound = false;
                    timer.setStop(true);
                    player.setMora(Player.READY);
                    countText.setText((String) msg.obj);
                    checkGameState();
                    break;
                case 5:
                    roundText.setText(getResources().getString(R.string.round) + " " + (gameRound + 1));
                    break;
            }
        }
    };

    public void nextRound() {
        if (isWin) {
            gameRoundSeconds -= (gameRound / 10) * 100;
            if (gameRoundSeconds < 1000) {
                gameRoundSeconds = 1000;
            }
        }
        timer.setTargetMilliseconds(gameRoundSeconds);
//        if (isWin) {
//            gameRoundSeconds = gameRoundSeconds < 300 ? 300 : gameRoundSeconds - (gameRound / 10) * 100;
//            timer.setTargetMilliseconds(gameRoundSeconds);
//        }
        gameRound += 1;
        handler.sendEmptyMessage(5);
        isPlayerRound = false;
        playerImage.setVisibility(View.INVISIBLE);
        isWin = false;
        timer.reset();
        Log.d(TAG, " gameRoundSeconds: " + gameRoundSeconds);

        computer.ai();
    }
}