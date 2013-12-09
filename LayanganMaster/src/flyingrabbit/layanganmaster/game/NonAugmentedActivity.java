package flyingrabbit.layanganmaster.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;


import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import flyingrabbit.layanganmaster.helper.Player;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.KeyEvent;

public class NonAugmentedActivity extends SimpleBaseGameActivity implements
		SensorEventListener {
	// ===========================================================
	// Constants
	// ===========================================================

	int cameraWidth;
	int cameraHeight;

	// ===========================================================
	// Fields
	// ===========================================================

	private ITextureRegion mKiteTextureRegion, mKite2TextureRegion,
			mKite3TextureRegion, mBoxTextureRegion, mCoinScoreTextureRegion,
			mButtonUpTextureRegion, mButtonDownTextureRegion,
			mBackgroundTextureRegion, mBgOverlayTextureRegion,
			mHomeTextureRegion;
	private Sprite kite, background, coinScore, powerBox1, powerBox2,
			powerBox3, buttonUp, buttonDown, fire1, fire2, fire3, water1,
			water2, water3, shield1, shield2, shield3, bgOverlay, home;
	private int accellerometerSpeedX;
	private int accellerometerSpeedY;
	private SensorManager sensorManager;
	private float centerX;
	private float centerY;
	private float bottomLimit, topLimit;
	private Camera camera;
	private Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas, mItemBitmapTextureAtlas;

	/* buat sprite burung */
	private BuildableBitmapTextureAtlas mBirdBitmapTextureAtlas;
	private TiledTextureRegion mBirdTextureRegionRight;
	private TiledTextureRegion mBirdTextureRegionLeft;

	/* untuk sprite coin */
	private ITextureRegion mCoinTextureRegion;
	private Sprite[] coinArray = new Sprite[50];
	private boolean[] coinActive = new boolean[50];
	int coinCollide = -1;
	int hitCount = 0;

	/* untuk sprite power item */
	private ITextureRegion mFireTextureRegion;
	private ITextureRegion mWaterTextureRegion;
	private ITextureRegion mShieldTextureRegion;

	private Sprite[] fireArray = new Sprite[30];
	private boolean[] fireActive = new boolean[30];
	private Sprite[] waterArray = new Sprite[30];
	private boolean[] waterActive = new boolean[30];
	private Sprite[] shieldArray = new Sprite[30];
	private boolean[] shieldActive = new boolean[30];

	int fireCollide = -1;
	int waterCollide = -1;
	int shieldCollide = -1;

	private String box1 = "kosong";
	private String box2 = "kosong";
	private String box3 = "kosong";
	private int pointer = 1;

	private int healthPoin = Menu.player.HP;

	private AnimatedSprite[] birdRightArray = new AnimatedSprite[30];
	private boolean[] birdRightActive = new boolean[30];
	private AnimatedSprite[] birdLeftArray = new AnimatedSprite[30];
	private boolean[] birdLeftActive = new boolean[30];

	int birdRightCollide = -1;
	int birdLeftCollide = -1;

	/* untuk text perolehan coin dan HP */
	private Font mFont, mGameOverFont, mFinalFont;
	private Text scoreText, healthPoinText;

	/* HUD game */
	private HUD gameHUD;

	private boolean upIsTouchedFlag = false;
	private boolean downIsTouchedFlag = false;
	private boolean gameOverDisplayed = false;

	private Text gameOverText, timesUpText, finalText;

	private double kecepatan = Menu.player.lari;

	/* punya rachmad */
	private ITextureRegion mHitArea, mCollidePoint, mTarik, mUlur, mVs1, mVs2,
			mVs3, mFire, mWater, mShield;

	final Scene scene2 = new Scene();

	Sprite hitArea1, collidePoint1, hitArea2, collidePoint2, tarik, ulur, vs,
			item1, item2, item3;
	// private Font mFont;
	private Text attackCountText, hpt, hpvt;
	private int attackCount = 0;

	private Sprite[] tombolBawah = new Sprite[20];
	private boolean[] isActive = new boolean[20];
	int collide = -1;

	private Sprite[] tombolAtas = new Sprite[20];
	private boolean[] isActive2 = new boolean[20];
	int collide2 = -1;

	private float startingDuration = 5.0f;

	int hit = 0, miss = 0;
	int attacked = 0;
	int attack = 0;
	int i = 0;
	int j = 0;

	int hp, hpv, atk, atkv, def, defv, spd, tL, bL;

	float position;

	boolean startVs, fire, water, shield, gameOver;

	/* buat sound */
	boolean isSoundOn = true;
	private Sound buttonClickedSound;
	private Sound hitClickedSound;
	private Sound missClickedSound;
	private Sound getCoinSound;
	private Sound getItemSound;
	private Sound hitBirdSound;
	private Sound usePowerSound;
	private Sound winSound;
	private Sound loseSound;

	Music music, musicBattle;

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						cameraWidth, cameraHeight), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		isSoundOn = Menu.player.isSound;

		survivalResource();
		createSfx();

		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "Averia-Regular.ttf", 14f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();

		mGameOverFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "Ding Dong Daddyo NF.ttf", 30f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mGameOverFont.load();

		mFinalFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "Candara.ttf", 22f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFinalFont.load();

		try {
			music = MusicFactory.createMusicFromAsset(
					mEngine.getMusicManager(), this, "msc/survival.mp3");
			music.setLooping(true);

			musicBattle = MusicFactory.createMusicFromAsset(
					mEngine.getMusicManager(), this, "msc/battle.ogg");
			music.setLooping(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	float scale, paddingY;

	@Override
	protected Scene onCreateScene() {
		survivalScene();

		// TODO Auto-generated method stub
		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private void createSfx() {
		SoundFactory.setAssetBasePath("sfx/");
		try {
			this.buttonClickedSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "buttonclick.wav");
			this.getCoinSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "coin.ogg");
			this.getItemSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "get_item.mp3");
			this.missClickedSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "miss_arrow.mp3");
			this.hitClickedSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "hit_arrow.mp3");
			this.hitBirdSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "birdhit.wav");
			this.usePowerSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "power.wav");
			this.winSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "you_win.ogg");
			this.loseSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "you_lose.ogg");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void survivalResource() {
		// TODO Auto-generated method stub
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sky.jpg", 0,
						0);

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		try {
			ITexture bg_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets()
									.open("asset/score_overlaybg.png");
						}
					});

			ITexture kite_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite1.png");
						}
					});
			ITexture kite2_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite2.png");
						}
					});

			ITexture kite3_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite3.png");
						}
					});

			ITexture coin_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/coin.png");
						}
					});
			ITexture fire_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/fire.png");
						}
					});
			ITexture water_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/water.png");
						}
					});
			ITexture shield_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/shield.png");
						}
					});

			ITexture kotak_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kotak.png");
						}
					});

			ITexture buttonUp_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/ulur.png");
						}
					});

			ITexture buttonDown_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/tarik.png");
						}
					});
			ITexture coinIndicator_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/coin2.png");
						}
					});

			ITexture homeButton_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});

			bg_asset.load();
			kite_asset.load();
			kite2_asset.load();
			kite3_asset.load();
			coin_asset.load();
			fire_asset.load();
			water_asset.load();
			shield_asset.load();
			kotak_asset.load();
			buttonUp_asset.load();
			buttonDown_asset.load();
			coinIndicator_asset.load();
			homeButton_asset.load();

			mBgOverlayTextureRegion = TextureRegionFactory
					.extractFromTexture(bg_asset);
			mKiteTextureRegion = TextureRegionFactory
					.extractFromTexture(kite_asset);
			mKite2TextureRegion = TextureRegionFactory
					.extractFromTexture(kite2_asset);
			mKite3TextureRegion = TextureRegionFactory
					.extractFromTexture(kite3_asset);
			mCoinTextureRegion = TextureRegionFactory
					.extractFromTexture(coin_asset);
			mFireTextureRegion = TextureRegionFactory
					.extractFromTexture(fire_asset);
			mWaterTextureRegion = TextureRegionFactory
					.extractFromTexture(water_asset);
			mShieldTextureRegion = TextureRegionFactory
					.extractFromTexture(shield_asset);
			mBoxTextureRegion = TextureRegionFactory
					.extractFromTexture(kotak_asset);
			mButtonUpTextureRegion = TextureRegionFactory
					.extractFromTexture(buttonUp_asset);
			mButtonDownTextureRegion = TextureRegionFactory
					.extractFromTexture(buttonDown_asset);
			mCoinScoreTextureRegion = TextureRegionFactory
					.extractFromTexture(coinIndicator_asset);
			mHomeTextureRegion = TextureRegionFactory
					.extractFromTexture(homeButton_asset);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

		this.mBirdBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 512, 256, TextureOptions.NEAREST);
		this.mBirdTextureRegionRight = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBirdBitmapTextureAtlas, this,
						"bird1.png", 4, 1);
		this.mBirdTextureRegionLeft = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBirdBitmapTextureAtlas, this,
						"bird2.png", 4, 1);

		try {
			this.mBirdBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mBitmapTextureAtlas.load();
			this.mBirdBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void battleModeResource() {
		position = cameraWidth / 2f;
		// TODO Auto-generated method stub
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sky.jpg", 0,
						0);

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

		try {

			ITexture bg_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/overlaybg.png");
						}
					});
			ITexture collidepoint_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/checker.png");
						}
					});

			ITexture tarik_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/tarik.png");
						}
					});
			ITexture ulur_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/ulur.png");
						}
					});

			ITexture vs1_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/vs1.png");
						}
					});

			ITexture vs2_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/vs2.png");
						}
					});

			ITexture vs3_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/vs3.png");
						}
					});

			ITexture fire_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/fire.png");
						}
					});
			ITexture water_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/water.png");
						}
					});
			ITexture shield_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/shield.png");
						}
					});

			bg_asset.load();
			collidepoint_asset.load();
			tarik_asset.load();
			ulur_asset.load();
			vs1_asset.load();
			vs2_asset.load();
			vs3_asset.load();
			fire_asset.load();
			water_asset.load();
			shield_asset.load();

			mHitArea = TextureRegionFactory.extractFromTexture(bg_asset);
			mCollidePoint = TextureRegionFactory
					.extractFromTexture(collidepoint_asset);
			mTarik = TextureRegionFactory.extractFromTexture(tarik_asset);
			mUlur = TextureRegionFactory.extractFromTexture(ulur_asset);
			mVs1 = TextureRegionFactory.extractFromTexture(vs1_asset);
			mVs2 = TextureRegionFactory.extractFromTexture(vs2_asset);
			mVs3 = TextureRegionFactory.extractFromTexture(vs3_asset);
			mFire = TextureRegionFactory.extractFromTexture(fire_asset);
			mWater = TextureRegionFactory.extractFromTexture(water_asset);
			mShield = TextureRegionFactory.extractFromTexture(shield_asset);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void survivalScene() {
		scale = cameraWidth / 400f;
		paddingY = 0;

		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		sensorManager = (SensorManager) this
				.getSystemService(this.SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_GAME);

		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				updateKitePosition();
			}

			public void reset() {
				// TODO Auto-generated method stub
			}
		});

		centerX = cameraWidth / 2f;
		centerY = cameraHeight / 2f;

		bgOverlay = new Sprite(0, 0, mBgOverlayTextureRegion,
				getVertexBufferObjectManager());
		bgOverlay.setScale(scale);
		bgOverlay.setPosition(cameraWidth / 2,
				cameraHeight - bgOverlay.getHeightScaled() / 2 - paddingY);
		scene.attachChild(bgOverlay);

		if (Menu.player.kite == 2) {
			kite = new Sprite(centerX, centerY, mKite2TextureRegion,
					this.getVertexBufferObjectManager());
		} else if (Menu.player.kite == 3) {
			kite = new Sprite(centerX, centerY, mKite3TextureRegion,
					this.getVertexBufferObjectManager());
		} else {
			kite = new Sprite(centerX, centerY, mKiteTextureRegion,
					this.getVertexBufferObjectManager());
		}

		kite.setScale(scale);
		scene.attachChild(kite);

		gameHUD = new HUD();

		scoreText = new Text(0, 0, mFont, "      ",
				getVertexBufferObjectManager());
		scoreText.setScale(scale);
		scoreText.setPosition(cameraWidth - scoreText.getWidthScaled() / 2,
				cameraHeight - (bgOverlay.getHeightScaled() / 2) - paddingY);
		scene.attachChild(scoreText);

		healthPoinText = new Text(0, 0, mFont, "Health Poins: " + healthPoin,
				getVertexBufferObjectManager());
		healthPoinText.setScale(scale);
		healthPoinText.setPosition(healthPoinText.getWidthScaled() / 2,
				cameraHeight - (bgOverlay.getHeightScaled() / 2) - paddingY);
		scene.attachChild(healthPoinText);

		powerBox1 = new Sprite(0, 0, mBoxTextureRegion,
				this.getVertexBufferObjectManager());
		powerBox1.setScale(scale);
		powerBox1.setPosition((cameraWidth / 3),
				(powerBox1.getHeightScaled() / 2) + paddingY);
		scene.attachChild(powerBox1);

		powerBox2 = new Sprite(0, 0, mBoxTextureRegion,
				this.getVertexBufferObjectManager());
		powerBox2.setScale(scale);
		powerBox2.setPosition((cameraWidth / 2),
				(powerBox2.getHeightScaled() / 2) + paddingY);
		scene.attachChild(powerBox2);

		powerBox3 = new Sprite(0, 0, mBoxTextureRegion,
				this.getVertexBufferObjectManager());
		powerBox3.setScale(scale);
		powerBox3.setPosition((cameraWidth * 2 / 3),
				(powerBox3.getHeightScaled() / 2) + paddingY);
		scene.attachChild(powerBox3);

		coinScore = new Sprite(0, 0, mCoinScoreTextureRegion,
				getVertexBufferObjectManager());
		coinScore.setScale(scale);
		coinScore.setPosition(cameraWidth - scoreText.getWidthScaled()
				- coinScore.getWidthScaled(),
				cameraHeight - (coinScore.getHeightScaled() / 2) - paddingY);
		scene.attachChild(coinScore);

		topLimit = cameraHeight;
		bottomLimit = paddingY;

		fire1 = new Sprite(0, 0, mFireTextureRegion,
				this.getVertexBufferObjectManager());
		fire2 = new Sprite(0, 0, mFireTextureRegion,
				this.getVertexBufferObjectManager());
		fire3 = new Sprite(0, 0, mFireTextureRegion,
				this.getVertexBufferObjectManager());
		water1 = new Sprite(0, 0, mWaterTextureRegion,
				this.getVertexBufferObjectManager());
		water2 = new Sprite(0, 0, mWaterTextureRegion,
				this.getVertexBufferObjectManager());
		water3 = new Sprite(0, 0, mWaterTextureRegion,
				this.getVertexBufferObjectManager());
		shield1 = new Sprite(0, 0, mShieldTextureRegion,
				this.getVertexBufferObjectManager());
		shield2 = new Sprite(0, 0, mShieldTextureRegion,
				this.getVertexBufferObjectManager());
		shield3 = new Sprite(0, 0, mShieldTextureRegion,
				this.getVertexBufferObjectManager());

		fire1.setScale(scale);
		fire2.setScale(scale);
		fire3.setScale(scale);

		water1.setScale(scale);
		water2.setScale(scale);
		water3.setScale(scale);

		shield1.setScale(scale);
		shield2.setScale(scale);
		shield3.setScale(scale);

		timeLimitTimeHandler();
		createCoinSpriteTimeHandler();
		scene.registerUpdateHandler(coinHandler);
		createPowerSpriteTimeHandler();
		scene.registerUpdateHandler(powerHandler);
		createBirdSpriteTimeHandler();
		scene.registerUpdateHandler(birdHandler);
		createControllers();

		mEngine.registerUpdateHandler(new TimerHandler(1f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);

						if (isSoundOn) {
							music.play();
						}

					}
				}));

		mEngine.setScene(scene);
	}

	private void battleModeScene() {
		scene2.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		initVs();

		hitArea1 = new Sprite(0, 0, mHitArea, getVertexBufferObjectManager());
		hitArea1.setScale(Menu.player.scale);
		hitArea1.setPosition(cameraWidth / 2,
				cameraHeight - hitArea1.getHeightScaled() / 2);
		scene2.attachChild(hitArea1);

		collidePoint1 = new Sprite(0, 0, mCollidePoint,
				getVertexBufferObjectManager());
		collidePoint1.setScale(Menu.player.scale);
		collidePoint1.setPosition(cameraWidth - collidePoint1.getWidthScaled()
				/ 2, cameraHeight - collidePoint1.getHeightScaled() / 2);
		scene2.attachChild(collidePoint1);

		hitArea2 = new Sprite(0, 0, mHitArea, getVertexBufferObjectManager());
		hitArea2.setScale(Menu.player.scale);
		hitArea2.setPosition(cameraWidth / 2,
				cameraHeight - hitArea2.getHeightScaled() - 20
						* Menu.player.scale);
		scene2.attachChild(hitArea2);

		collidePoint2 = new Sprite(0, 0, mCollidePoint,
				getVertexBufferObjectManager());
		collidePoint2.setScale(Menu.player.scale);
		collidePoint2.setPosition(cameraWidth - collidePoint2.getWidthScaled()
				/ 2, cameraHeight - collidePoint2.getHeightScaled() - 20
				* Menu.player.scale);
		scene2.attachChild(collidePoint2);

		ulur = new Sprite(0, 0, mUlur, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pscene2TouchEvent, float X,
					float Y) {
				if (pscene2TouchEvent.isActionUp()) {
					if (collide2 >= 0) {
						attack += 5;
						hit++;
						scene2.detachChild(tombolAtas[collide2]);
						tombolAtas[collide2] = null;
						isActive2[collide2] = false;
						collide2 = -1;
						if (isSoundOn) {
							hitClickedSound.play();
						}
					} else {
						if (isSoundOn) {
							missClickedSound.play();
						}
						miss++;
						attacked += 2;
					}
				}
				return true;
			};
		};

		ulur.setScale(Menu.player.scale);
		ulur.setPosition(cameraWidth - ulur.getWidthScaled() / 2,
				ulur.getHeightScaled() / 2);
		scene2.attachChild(ulur);
		scene2.registerTouchArea(ulur);

		tarik = new Sprite(0, 0, mTarik, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pscene2TouchEvent, float X,
					float Y) {

				if (pscene2TouchEvent.isActionUp()) {
					if (collide >= 0) {
						attack += 5;
						hit++;
						scene2.detachChild(tombolBawah[collide]);
						tombolBawah[collide] = null;
						isActive[collide] = false;
						collide = -1;
						if (isSoundOn) {
							hitClickedSound.play();
						}
					} else {
						if (isSoundOn) {
							missClickedSound.play();
						}
						miss++;
						attacked += 2;
					}
				}

				return true;
			};
		};

		tarik.setScale(Menu.player.scale);
		tarik.setPosition(tarik.getWidthScaled() / 2,
				tarik.getHeightScaled() / 2);
		scene2.attachChild(tarik);
		scene2.registerTouchArea(tarik);

		scene2.registerUpdateHandler(new TimerHandler(0.01f, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						// TODO Auto-generated method stub
						if (fire) {
							if (position < cameraWidth - vs.getWidthScaled()
									/ 2) {
								position += 1f;
								vs.setX(position);

							} else {
								j++;
								if (j < 5) {
									hpv = Math.max(0, hpv - atk
											* (4 - Menu.player.exp / 300));
									Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
									v.vibrate(300);
									hpt.setText("HP : " + hp);
									hpvt.setText("HP : " + hpv);
									if (j != 4) {
										position = cameraWidth * 0.75f;
									}
								} else {
									fire = false;
									j = 0;
								}
							}
						} else if (water) {
							if (position < cameraWidth - vs.getWidthScaled()
									/ 2) {
								position += 1f;
								vs.setX(position);

							} else {
								hpv = Math.max(0, hpv - atk
										* (5 - Menu.player.exp / 300));
								Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								v.vibrate(300);
								hpt.setText("HP : " + hp);
								hpvt.setText("HP : " + hpv);
								water = false;
							}
						} else if (shield) {
							j++;
							if (j == 300) {
								j = 0;
								shield = false;
							}

						}
					}
				}));

		this.createSpriteSpawnTimeHandler();

		scene2.registerUpdateHandler(new TimerHandler(0.02f, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						// TODO Auto-generated method stub

						if (!fire && !water) {
							if (hp <= 0 || hpv <= 0) {
								if (!gameOver && startVs) {
									gameOver = true;
									if (hp != 0) {
										win();

									} else {
										lose();

									}
								}

							}

							boolean active = false;
							if (attack > 0) {
								active = true;
								if (position < cameraWidth
										- vs.getWidthScaled() / 2) {
									position += 3f;
									vs.setX(position);
								}
								attack--;
							}

							if (attack == 0 && active) {
								if (position > cameraWidth / 2) {
									if (position < cameraWidth
											- vs.getWidthScaled() / 2) {
										hpv = Math.max(0, hpv - atk
												* (3 - Menu.player.exp / 300));
									} else {
										hpv = Math.max(0, hpv - atk
												* (5 - Menu.player.exp / 300));
									}
								}
								active = false;
								Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								v.vibrate(300);
							}
							hpt.setText("HP : " + hp);
							hpvt.setText("HP : " + hpv);
						}
					}

				}));

		scene2.registerUpdateHandler(new TimerHandler(0.04f, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						// TODO Auto-generated method stub
						if (!fire && !water && !shield) {
							if (hp <= 0 || hpv <= 0) {
								if (!gameOver && startVs) {
									gameOver = true;
									if (hp != 0) {
										win();

									} else {
										lose();

									}
								}

							}
							boolean active = false;

							if (startVs) {
								attacked++;

								if (attacked > 0) {
									i++;
									if (position > vs.getWidthScaled() / 2) {
										position -= 2f;
										vs.setX(position);
									}
									attacked -= 2;
								}

								if (i >= 10) {
									i = 0;
									active = true;
								}

								if (attacked <= 0 && active) {
									if (position < cameraWidth / 2) {
										if (position > vs.getWidthScaled() / 2) {
											hp = Math.max(
													0,
													hp
															- Math.max(
																	1,
																	atkv
																			* (3 - Menu.player.exp / 300)
																			- def));
										} else {
											hp = Math.max(
													0,
													(int) (hp - Math
															.max(1,
																	atkv
																			* (5 - Menu.player.exp / 300)
																			- def)));
											Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
											v.vibrate(300);
										}
									}
									active = false;
								}
							}
							hpt.setText("HP : " + hp);
							hpvt.setText("HP : " + hpv);
						}
					}

				}));

		scene2.registerUpdateHandler(new TimerHandler(0.1f, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						// TODO Auto-generated method stub

						collide = -1;
						for (int i = 0; i < 20; i++) {
							if (tombolBawah[i] != null) {
								if (tombolBawah[i].getX() > cameraWidth + 50) {
									scene2.detachChild(tombolBawah[i]);
									tombolBawah[i] = null;
									isActive[i] = false;
									miss++;
									attacked += 10;
								} else if (tombolBawah[i]
										.collidesWith(collidePoint2)) {
									startVs = true;
									collide = i;
									break;
								}
							}
						}

						collide2 = -1;
						for (int i = 0; i < 20; i++) {
							if (tombolAtas[i] != null) {
								if (tombolAtas[i].getX() > cameraWidth + 50) {
									scene2.detachChild(tombolAtas[i]);
									tombolAtas[i] = null;
									isActive2[i] = false;
									miss++;
									attacked += 10;
								} else if (tombolAtas[i]
										.collidesWith(collidePoint1)) {
									startVs = true;
									collide2 = i;
									break;
								}
							}
						}

					}
				}));

		hpt = new Text(100 * Menu.player.scale, cameraHeight * 0.3f, mFont,
				"HP : " + hp, getVertexBufferObjectManager());
		hpt.setScale(Menu.player.scale);
		scene2.attachChild(hpt);

		hpvt = new Text(cameraWidth - 100 * Menu.player.scale,
				cameraHeight * 0.3f, mFont, "HP : " + hpv,
				getVertexBufferObjectManager());
		hpvt.setScale(Menu.player.scale);
		scene2.attachChild(hpvt);

		mEngine.setScene(scene2);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				accellerometerSpeedX = (int) event.values[1];
				accellerometerSpeedY = (int) event.values[0];
				break;
			}
		}

	}

	private void updateKitePosition() {
		if ((accellerometerSpeedX != 0) || (accellerometerSpeedY != 0)) {
			// Set the Boundary limits
			int lL = (int) kite.getWidthScaled() / 2;
			int rL = cameraWidth - (int) kite.getWidthScaled() / 2;
			// Calculate New X,Y Coordinates within Limits
			if (centerX >= lL)
				centerX += accellerometerSpeedX * scale;
			else
				centerX = lL;
			if (centerX <= rL)
				centerX += accellerometerSpeedX * scale;
			else
				centerX = rL;
			if (centerX < lL)
				centerX = lL;
			else if (centerX > rL)
				centerX = rL;
			kite.setX(centerX);
		}
	}

	TimerHandler timeLimit;

	private void timeLimitTimeHandler() {

		float limit = 30f;
		timeLimit = new TimerHandler(limit, true, new ITimerCallback() {

			public void onTimePassed(TimerHandler pTimerHandler) {
				onTimesUp();
			}
		});
		scene.registerUpdateHandler(timeLimit);
	}

	public void onTimesUp() {
		scene.unregisterUpdateHandler(timeLimit);
		if (!gameOverDisplayed) {
			displayTimesUpText();
			mEngine.registerUpdateHandler(new TimerHandler(2f, true,
					new ITimerCallback() {
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {

							mEngine.unregisterUpdateHandler(pTimerHandler);
							if (isSoundOn) {
								music.stop();
							}
							if (healthPoin <= 0) {
								displayGameOverText(false);
							} else if (healthPoin > 0) {

								if (isSoundOn) {
									musicBattle.setVolume(1);
									musicBattle.play();
								}

								scene.clearUpdateHandlers();
								scene.clearTouchAreas();
								scene.detachChildren();

								camera.getHUD().clearUpdateHandlers();
								camera.getHUD().clearTouchAreas();
								camera.getHUD().detachChildren();

								battleModeResource();
								battleModeScene();
							}
						}
					}));

		}
	}

	public void displayTimesUpText() {
		Scene timesUp = new Scene();

		timesUpText = new Text(0, 0, mGameOverFont, "Saatnya Bertarung !",
				getVertexBufferObjectManager());
		timesUpText.setScale(scale);
		timesUpText.setPosition(cameraWidth / 2,
				cameraHeight / 2 + timesUpText.getHeightScaled() / 2);
		timesUp.attachChild(timesUpText);

		finalText = new Text(0, 0, mFinalFont, "Dapat Koin: "
				+ String.valueOf(hitCount), getVertexBufferObjectManager());
		finalText.setScale(scale);
		finalText.setPosition(cameraWidth / 2,
				cameraHeight / 2 - finalText.getHeightScaled());
		timesUp.attachChild(finalText);

		gameHUD.detachChildren();

		mEngine.setScene(timesUp);

	}

	private void createCoinSpriteTimeHandler() {
		TimerHandler coinTimerHandler;

		float mEffectSpawnDelay = (float) (3f - Math.max(2f, kecepatan / 20));

		coinTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						addCoin();
					}
				});

		scene.registerUpdateHandler(coinTimerHandler);
	}

	private void addCoin() {
		Random rand = new Random();

		float y = cameraHeight + mCoinTextureRegion.getHeight();
		float minX = mCoinTextureRegion.getWidth();
		float maxX = cameraWidth - mCoinTextureRegion.getWidth();
		float rangeX = maxX - minX;
		float x = rand.nextFloat() * rangeX + minX;

		Sprite coin = null;
		for (int i = 0; i < 50; i++) {
			if (!coinActive[i]) {
				coinActive[i] = true;
				coinArray[i] = new Sprite(x, y,
						this.mCoinTextureRegion.deepCopy(),
						this.getVertexBufferObjectManager());
				coinArray[i].setScale(scale * 0.8f);
				coin = coinArray[i];
				break;
			}
		}

		/* Coin duration */
		int minDuration = 2;
		int maxDuration = 3;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;

		MoveYModifier mod = new MoveYModifier(actualDuration, cameraHeight
				- bgOverlay.getHeightScaled() - paddingY, -100);

		this.scene.attachChild(coin);
		coin.registerEntityModifier(mod.deepCopy());
	}

	IUpdateHandler coinHandler = new IUpdateHandler() {
		public void reset() {
		}

		public void onUpdate(final float pSecondsElapsed) {

			coinCollide = -1;
			for (int i = 0; i < 50; i++) {
				if (coinArray[i] != null) {
					if (coinArray[i].getY() < bottomLimit) {
						scene.detachChild(coinArray[i]);
						coinArray[i] = null;
						coinActive[i] = false;
					} else if (coinArray[i].collidesWith(kite)) {
						if (isSoundOn) {
							getCoinSound.play();
						}
						scene.detachChild(coinArray[i]);
						coinArray[i] = null;
						coinActive[i] = false;
						coinCollide = i;
						hitCount += 50;
						scoreText.setText(String.valueOf(hitCount));
					}
				}
			}
		}
	};

	private void createPowerSpriteTimeHandler() {
		TimerHandler powerTimerHandler;
		float mEffectSpawnDelay = 3f;

		powerTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						addPower();
					}
				});

		scene.registerUpdateHandler(powerTimerHandler);
	}

	public void addPower() {
		Random rand = new Random();
		int tmp = rand.nextInt(2);
		if (tmp == 1) {
			int type = rand.nextInt(3);
			if (type == 0) {
				float y = cameraHeight + mFireTextureRegion.getHeight();
				float minX = mFireTextureRegion.getWidth();
				float maxX = cameraWidth - mFireTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;
				Sprite Fire = null;

				for (int i = 0; i < 30; i++) {
					if (!fireActive[i]) {
						fireActive[i] = true;
						fireArray[i] = new Sprite(x, y,
								this.mFireTextureRegion.deepCopy(),
								this.getVertexBufferObjectManager());
						fireArray[i].setScale(scale);
						Fire = fireArray[i];
						break;
					}
				}

				int minDurationFire = 2;
				int maxDurationFire = 4;
				int rangeDurationFire = maxDurationFire - minDurationFire;
				int actualDurationFire = rand.nextInt(rangeDurationFire)
						+ minDurationFire;

				MoveYModifier modFire = new MoveYModifier(actualDurationFire,
						cameraHeight - bgOverlay.getHeightScaled() - paddingY,
						-100);

				this.scene.attachChild(Fire);
				Fire.registerEntityModifier(modFire.deepCopy());
			} else if (type == 1) {
				float y = cameraHeight + mWaterTextureRegion.getHeight();
				float minX = mWaterTextureRegion.getWidth();
				float maxX = cameraWidth - mWaterTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;

				Sprite Water = null;

				for (int i = 0; i < 30; i++) {
					if (!waterActive[i]) {
						waterActive[i] = true;
						waterArray[i] = new Sprite(x, y,
								this.mWaterTextureRegion.deepCopy(),
								this.getVertexBufferObjectManager());
						waterArray[i].setScale(scale);
						Water = waterArray[i];
						break;
					}
				}

				int minDurationWater = 2;
				int maxDurationWater = 4;
				int rangeDurationWater = maxDurationWater - minDurationWater;
				int actualDurationWater = rand.nextInt(rangeDurationWater)
						+ minDurationWater;

				MoveYModifier modWater = new MoveYModifier(actualDurationWater,
						cameraHeight - bgOverlay.getHeightScaled() - paddingY,
						-100);
				this.scene.attachChild(Water);
				Water.registerEntityModifier(modWater.deepCopy());
			} else if (type == 2) {
				float y = cameraHeight + mShieldTextureRegion.getHeight();
				float minX = mShieldTextureRegion.getWidth();
				float maxX = cameraWidth - mShieldTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;

				Sprite Shield = null;

				for (int i = 0; i < 30; i++) {
					if (!shieldActive[i]) {
						shieldActive[i] = true;
						shieldArray[i] = new Sprite(x, y,
								this.mShieldTextureRegion.deepCopy(),
								this.getVertexBufferObjectManager());
						shieldArray[i].setScale(scale);
						Shield = shieldArray[i];
						break;
					}
				}

				int minDurationShield = 2;
				int maxDurationShield = 4;
				int rangeDurationShield = maxDurationShield - minDurationShield;
				int actualDurationShield = rand.nextInt(rangeDurationShield)
						+ minDurationShield;

				MoveYModifier modShield = new MoveYModifier(
						actualDurationShield, cameraHeight
								- bgOverlay.getHeightScaled() - paddingY, -100);
				this.scene.attachChild(Shield);
				Shield.registerEntityModifier(modShield.deepCopy());
			}
		}
	}

	IUpdateHandler powerHandler = new IUpdateHandler() {

		@Override
		public void reset() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub

			fireCollide = -1;
			for (int i = 0; i < 30; i++) {
				if (fireArray[i] != null) {
					if (fireArray[i].getY() < bottomLimit) {
						scene.detachChild(fireArray[i]);
						fireArray[i] = null;
						fireActive[i] = false;
					} else if (fireArray[i].collidesWith(kite)) {
						if (isSoundOn) {
							getItemSound.play();
						}
						scene.detachChild(fireArray[i]);
						fireArray[i] = null;
						fireActive[i] = false;
						fireCollide = i;
						fireAddToBox();
					}
				}
			}

			waterCollide = -1;
			for (int i = 0; i < 30; i++) {
				if (waterArray[i] != null) {
					if (waterArray[i].getY() < bottomLimit) {
						scene.detachChild(waterArray[i]);
						waterArray[i] = null;
						waterActive[i] = false;
					} else if (waterArray[i].collidesWith(kite)) {
						if (isSoundOn) {
							getItemSound.play();
						}
						scene.detachChild(waterArray[i]);
						waterArray[i] = null;
						waterActive[i] = false;
						waterCollide = i;
						waterAddToBox();
					}
				}
			}

			shieldCollide = -1;
			for (int i = 0; i < 30; i++) {
				if (shieldArray[i] != null) {
					if (shieldArray[i].getY() < bottomLimit) {
						scene.detachChild(shieldArray[i]);
						shieldArray[i] = null;
						shieldActive[i] = false;
					} else if (shieldArray[i].collidesWith(kite)) {
						if (isSoundOn) {
							getItemSound.play();
						}
						scene.detachChild(shieldArray[i]);
						shieldArray[i] = null;
						shieldActive[i] = false;
						shieldCollide = i;
						shieldAddToBox();
					}
				}
			}
		}
	};

	private void createBirdSpriteTimeHandler() {
		TimerHandler birdTimerHandler;
		float mEffectSpawnDelay = 3f;

		birdTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {
					public void onTimePassed(TimerHandler pTimerHandler) {
						addBird();
					}
				});

		scene.registerUpdateHandler(birdTimerHandler);
	}

	public void addBird() {
		Random rand = new Random();
		float x = cameraWidth + mBirdTextureRegionRight.getWidth();
		float minY = mBirdTextureRegionRight.getHeight();
		float maxY = cameraHeight - mBirdTextureRegionRight.getHeight();
		float rangeY = maxY - minY;
		float y = rand.nextFloat() * rangeY + minY;

		float a = cameraWidth + mBirdTextureRegionLeft.getWidth();
		float minB = mBirdTextureRegionLeft.getHeight();
		float maxB = cameraHeight - mBirdTextureRegionLeft.getHeight();
		float rangeB = maxB - minB;
		float b = rand.nextFloat() * rangeB + minB;

		AnimatedSprite birdRight = null;
		for (int i = 0; i < 30; i++) {
			if (!birdRightActive[i]) {
				birdRightActive[i] = true;
				birdRightArray[i] = new AnimatedSprite(x, y,
						this.mBirdTextureRegionRight.deepCopy(),
						this.getVertexBufferObjectManager());
				birdRightArray[i].setScale(scale);
				birdRightArray[i].animate(100);
				birdRight = birdRightArray[i];
				break;
			}
		}

		AnimatedSprite birdLeft = null;
		for (int i = 0; i < 30; i++) {
			if (!birdLeftActive[i]) {
				birdLeftActive[i] = true;
				birdLeftArray[i] = new AnimatedSprite(a, b,
						this.mBirdTextureRegionLeft.deepCopy(),
						this.getVertexBufferObjectManager());
				birdLeftArray[i].setScale(scale);
				birdLeftArray[i].animate(100);
				birdLeft = birdLeftArray[i];
				break;
			}
		}

		int minDuration = 3;
		int maxDuration = 6;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;

		int minLeft = 3;
		int maxLeft = 5;
		int rangeLeft = maxLeft - minLeft;
		int actualLeft = rand.nextInt(rangeLeft) + minLeft;

		MoveXModifier modRight = new MoveXModifier(actualDuration, -100,
				cameraWidth + 100);

		MoveXModifier modLeft = new MoveXModifier(actualLeft,
				cameraWidth, -100);

		birdRight.registerEntityModifier(modRight.deepCopy());
		birdLeft.registerEntityModifier(modLeft.deepCopy());
		scene.attachChild(birdLeft);
		scene.attachChild(birdRight);
		
	}

	IUpdateHandler birdHandler = new IUpdateHandler() {
		public void reset() {
		}

		public void onUpdate(final float pSecondsElapsed) {

			birdRightCollide = -1;
			for (int i = 0; i < 30; i++) {
				if (birdRightArray[i] != null) {
					if (birdRightArray[i].getX() > cameraWidth + 50) {
						scene.detachChild(birdRightArray[i]);
						birdRightArray[i] = null;
						birdRightActive[i] = false;
					} else if (birdRightArray[i].collidesWith(kite)) {
						if (isSoundOn) {
							hitBirdSound.play();
						}
						Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						v.vibrate(300);
						scene.detachChild(birdRightArray[i]);
						birdRightArray[i] = null;
						birdRightActive[i] = false;
						birdRightCollide = i;
						healthPoin -= 10;
						healthPoinText.setText("Health Poin: "
								+ String.valueOf(healthPoin));
					}
				}
			}

			birdLeftCollide = -1;
			for (int i = 0; i < 30; i++) {
				if (birdLeftArray[i] != null) {
					if (birdLeftArray[i].getX() > cameraWidth + 50) {
						scene.detachChild(birdLeftArray[i]);
						birdLeftArray[i] = null;
						birdLeftActive[i] = false;
					} else if (birdLeftArray[i].collidesWith(kite)) {
						if (isSoundOn) {
							hitBirdSound.play();
						}
						Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						v.vibrate(300);
						scene.detachChild(birdLeftArray[i]);
						birdLeftArray[i] = null;
						birdLeftActive[i] = false;
						birdLeftCollide = i;
						healthPoin -= 10;
						healthPoinText.setText("Health Poin: "
								+ String.valueOf(healthPoin));
					}

					if (healthPoin <= 0) {
						onDie();
					}
				}
			}

		}
	};

	public void fireAddToBox() {
		if (pointer > 3) {
			pointer = 1;
		}
		if (pointer == 1) {
			if (box1 == "kosong") {
				fire1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire1);
			} else if (box1 == "water") {
				scene.detachChild(water1);
				fire1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire1);
			} else if (box1 == "shield") {
				scene.detachChild(shield1);
				fire1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire1);
			}
			box1 = "fire";
			++pointer;
		} else if (pointer == 2) {
			if (box2 == "kosong") {
				fire2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire2);
			} else if (box2 == "water") {
				scene.detachChild(water2);
				fire2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire2);
			} else if (box2 == "shield") {
				scene.detachChild(shield2);
				fire2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire2);
			}
			box2 = "fire";
			++pointer;
		} else if (pointer == 3) {
			if (box3 == "kosong") {
				fire3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire3);
			} else if (box3 == "water") {
				scene.detachChild(water3);
				fire3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire3);
			} else if (box3 == "shield") {
				scene.detachChild(shield3);
				fire3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(fire3);
			}
			box3 = "fire";
			++pointer;
		}
	}

	public void waterAddToBox() {
		if (pointer > 3) {
			pointer = 1;
		}
		if (pointer == 1) {
			if (box1 == "kosong") {
				water1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water1);
			} else if (box1 == "fire") {
				scene.detachChild(fire1);
				water1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water1);
			} else if (box1 == "shield") {
				scene.detachChild(shield1);
				water1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water1);
			}
			box1 = "water";
			++pointer;
		} else if (pointer == 2) {
			if (box2 == "kosong") {
				water2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water2);
			} else if (box2 == "fire") {
				scene.detachChild(fire2);
				water2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water2);
			} else if (box2 == "shield") {
				scene.detachChild(shield2);
				water2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water2);
			}
			box2 = "water";
			++pointer;
		} else if (pointer == 3) {
			if (box3 == "kosong") {
				water3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water3);
			} else if (box3 == "fire") {
				scene.detachChild(fire3);
				water3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water3);
			} else if (box3 == "shield") {
				scene.detachChild(shield3);
				water3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(water3);
			}
			box3 = "water";
			++pointer;
		}
	}

	public void shieldAddToBox() {
		if (pointer > 3) {
			pointer = 1;
		}
		if (pointer == 1) {
			if (box1 == "kosong") {
				shield1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield1);
			} else if (box1 == "fire") {
				scene.detachChild(fire1);
				shield1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield1);
			} else if (box1 == "water") {
				scene.detachChild(water1);
				shield1.setPosition(
						cameraWidth / 3 - (powerBox1.getScaleCenterX() / 2),
						(powerBox1.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield1);
			}
			box1 = "shield";
			++pointer;
		} else if (pointer == 2) {
			if (box2 == "kosong") {
				shield2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield2);
			} else if (box2 == "fire") {
				scene.detachChild(fire2);
				shield2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield2);
			} else if (box2 == "water") {
				scene.detachChild(water2);
				shield2.setPosition(
						cameraWidth / 2 - (powerBox2.getScaleCenterX() / 2),
						(powerBox2.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield2);
			}
			box2 = "shield";
			++pointer;
		} else if (pointer == 3) {
			if (box3 == "kosong") {
				shield3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield3);
			} else if (box3 == "fire") {
				scene.detachChild(fire3);
				shield3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield3);
			} else if (box3 == "water") {
				scene.detachChild(water3);
				shield3.setPosition(
						cameraWidth * 2 / 3 - (powerBox3.getScaleCenterX() / 2),
						(powerBox3.getHeightScaled() / 2) + paddingY);
				scene.attachChild(shield3);
			}
			box3 = "shield";
			++pointer;
		}
	}

	/* Button Controller */
	TimerHandler tButton;
	boolean isUp, isDown;

	/* Button Controller */
	private void createControllers() {
		buttonUp = new ButtonSprite(0, 0, mButtonUpTextureRegion,
				getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					isUp = true;
					isDown = false;

					if (isSoundOn) {
						buttonClickedSound.play();
					}

				}

				if (pSceneTouchEvent.isActionUp()) {
					isUp = false;

				}
				return true;
			}
		};
		buttonUp.setScale(scale);
		buttonUp.setPosition(cameraWidth - buttonUp.getWidthScaled() / 2,
				buttonUp.getHeightScaled() / 2 + paddingY);

		buttonDown = new ButtonSprite(0, 0, mButtonDownTextureRegion,
				getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					isUp = false;
					isDown = true;
					if (isSoundOn) {
						buttonClickedSound.play();
					}

				}
				if (pSceneTouchEvent.isActionUp()) {
					isDown = false;
				}
				return true;
			}
		};
		buttonDown.setScale(scale);
		buttonDown.setPosition(buttonDown.getWidthScaled() / 2,
				buttonDown.getHeightScaled() / 2 + paddingY);

		tL = cameraHeight - (int) kite.getHeightScaled();
		bL = (int) buttonUp.getHeightScaled();

		/* Handler untuk button */
		tButton = new TimerHandler(0.007f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (isUp && !isDown) {
					if (centerY >= tL) {
						centerY = tL;
					} else {
						centerY += 2;

					}
				} else if (isDown && !isUp) {
					if (centerY <= bL) {
						centerY = bL;
					} else {
						centerY -= 2;

					}
				}
				kite.setY(centerY);
			}
		});

		scene.registerUpdateHandler(tButton);

		gameHUD.registerTouchArea(buttonUp);
		gameHUD.registerTouchArea(buttonDown);
		gameHUD.attachChild(buttonUp);
		gameHUD.attachChild(buttonDown);
		camera.setHUD(gameHUD);
	}

	public void displayGameOverText(boolean win) {
		final Scene gameOver = new Scene();

		if (win) {
			gameOverText = new Text(0, 0, mGameOverFont,
					"Permainan Berakhir! \n\n Yeay Menang !",
					getVertexBufferObjectManager());
		} else {
			gameOverText = new Text(0, 0, mGameOverFont,
					"Permainan Berakhir! \n\n Yaah Kalah :<",
					getVertexBufferObjectManager());
		}
		gameOverText.setScale(scale);
		gameOverText.setPosition(cameraWidth / 2, cameraHeight / 2);
		gameOver.attachChild(gameOverText);
		gameOver.registerUpdateHandler(new TimerHandler(3f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						gameOver.clearUpdateHandlers();
						Menu.menuActivity.finish();
						startActivity(new Intent(getApplicationContext(),
								Menu.class));
						finish();
					}
				}));
		mEngine.setScene(gameOver);

	}

	public void onDie() {
		if (!gameOverDisplayed) {
			displayGameOverText(false);
		}

	}

	private void win() {
		if (isSoundOn) {
			musicBattle.stop();
		}
		int old = Menu.player.level;
		Menu.player.updateExp(Menu.player.exp + 20);
		Menu.player.updatePower(Menu.player.attack + 3,
				Menu.player.defense + 3, Menu.player.speed + 5,
				Menu.player.HP + 350);
		if (old != 3 && Menu.player.level == 3) {
			startActivity(new Intent(getApplicationContext(), EndStory.class));
			finish();
		} else {
			if (isSoundOn) {
				winSound.play();
			}
			Menu.player.updateCoin(Menu.player.coin + hitCount);
			displayGameOverText(true);

		}
	}

	private void lose() {
		if (isSoundOn) {
			musicBattle.stop();
		}
		int old = Menu.player.level;
		Menu.player.updateExp(Menu.player.exp + 5);
		Menu.player.updatePower(Menu.player.attack + 3,
				Menu.player.defense + 1, Menu.player.speed + 1,
				Menu.player.HP + 150);
		if (old != 3 && Menu.player.level == 3) {
			startActivity(new Intent(getApplicationContext(), EndStory.class));
			finish();
		} else {
			if (isSoundOn) {
				loseSound.play();
			}
			Menu.player.updateCoin(Menu.player.coin + hitCount);
			displayGameOverText(false);
		}
	}

	private void initVs() {
		hp = healthPoin;
		atk = Menu.player.attack;
		def = Menu.player.defense;
		spd = Menu.player.speed;
		hpv = (int) ((1.05 + Menu.player.exp / 1000) * hp);
		atkv = (int) ((1.25 + Menu.player.exp / 1200) * atk);

		if (Menu.player.kite == 2) {
			atk += 10;
			def += 30;
			spd += 5;
			vs = new Sprite(0, 0, mVs2, getVertexBufferObjectManager());
		} else if (Menu.player.kite == 3) {
			atk += 20;
			def += 20;
			spd += 10;
			vs = new Sprite(0, 0, mVs3, getVertexBufferObjectManager());
		} else {
			vs = new Sprite(0, 0, mVs1, getVertexBufferObjectManager());
		}

		if (Menu.player.buyBlue) {
			atk += 10;
			def += 5;
			spd += 5;
		}

		vs.setScale(Menu.player.scale);
		vs.setPosition(cameraWidth / 2, cameraHeight / 2);
		scene2.attachChild(vs);

		String i1 = box1;
		String i2 = box2;
		String i3 = box3;

		if (!i1.equals("kosong")) {
			if (i1.equals("fire")) {
				item1 = new Sprite(0, 0, mFire, getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								fire = true;
								scene2.detachChild(item1);
							}
						}

						return true;
					};
				};
			} else if (i1.equals("water")) {
				item1 = new Sprite(0, 0, mWater, getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								water = true;
								scene2.detachChild(item1);
							}
						}

						return true;
					};
				};
			} else if (i1.equals("shield")) {
				item1 = new Sprite(0, 0, mShield,
						getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								shield = true;
								scene2.detachChild(item1);
							}
						}

						return true;
					};
				};
			}
			item1.setScale(Menu.player.scale);
			item1.setPosition(cameraWidth / 3, item1.getHeightScaled() / 2
					+ Menu.player.paddingY);
			scene2.attachChild(item1);
			scene2.registerTouchArea(item1);
		}

		if (!i2.equals("kosong")) {
			if (i2.equals("fire")) {
				item2 = new Sprite(0, 0, mFire, getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								fire = true;
								scene2.detachChild(item2);
							}
						}

						return true;
					};
				};
			} else if (i2.equals("water")) {
				item2 = new Sprite(0, 0, mWater, getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								water = true;
								scene2.detachChild(item2);
							}
						}

						return true;
					};
				};
			} else if (i2.equals("shield")) {
				item2 = new Sprite(0, 0, mShield,
						getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								shield = true;
								scene2.detachChild(item2);
							}
						}

						return true;
					};
				};
			}
			item2.setScale(Menu.player.scale);
			item2.setPosition(cameraWidth / 2, item2.getHeightScaled() / 2
					+ Menu.player.paddingY);
			scene2.attachChild(item2);
			scene2.registerTouchArea(item2);
		}

		if (!i3.equals("kosong")) {
			if (i3.equals("fire")) {
				item3 = new Sprite(0, 0, mFire, getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								fire = true;
								scene2.detachChild(item3);
							}
						}

						return true;
					};
				};
			} else if (i3.equals("water")) {
				item3 = new Sprite(0, 0, mWater, getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								water = true;
								scene2.detachChild(item3);
							}
						}

						return true;
					};
				};
			} else if (i3.equals("shield")) {
				item3 = new Sprite(0, 0, mShield,
						getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(TouchEvent pscene2TouchEvent,
							float X, float Y) {

						if (pscene2TouchEvent.isActionDown()) {
							if (startVs) {
								if (isSoundOn) {
									usePowerSound.play();
								}
								shield = true;
								scene2.detachChild(item3);
							}
						}

						return true;
					};
				};
			}

			item3.setScale(Menu.player.scale);
			item3.setPosition(cameraWidth * 2 / 3, item3.getHeightScaled() / 2
					+ Menu.player.paddingY);
			scene2.attachChild(item3);
			scene2.registerTouchArea(item3);
		}

	}

	private void createSpriteSpawnTimeHandler() {
		TimerHandler spriteTimerHandler;
		float mEffectSpawnDelay = 0.7f - spd / 300f;

		spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						addArrow();
					}
				});

		scene2.registerUpdateHandler(spriteTimerHandler);
	}

	public void addArrow() {
		Random rand = new Random();
		float tempArrow = rand.nextFloat();
		float actualDuration = startingDuration;

		if (hit >= 10) {
			hit = 0;
			if (startingDuration > 2f)
				startingDuration -= 0.7f;
		}

		if (miss >= 5) {
			miss = 0;
			if (startingDuration < 5f)
				startingDuration += 0.7f;
		}

		Sprite arrow = null;
		if (tempArrow <= 0.5) {
			for (int i = 0; i < 20; i++) {
				if (!isActive[i]) {
					isActive[i] = true;
					tombolBawah[i] = new Sprite(-100, cameraHeight
							- hitArea2.getHeightScaled() - 20
							* Menu.player.scale, this.mTarik.deepCopy(),
							this.getVertexBufferObjectManager());
					tombolBawah[i].setScale(Menu.player.scale / 2);
					arrow = tombolBawah[i];
					break;
				}
			}

			MoveXModifier mod = new MoveXModifier(actualDuration, -100,
					cameraWidth + 100);
			scene2.attachChild(arrow);
			arrow.registerEntityModifier(mod.deepCopy());
		} else {
			for (int i = 0; i < 20; i++) {
				if (!isActive2[i]) {
					isActive2[i] = true;
					tombolAtas[i] = new Sprite(-100, cameraHeight
							- hitArea1.getHeightScaled() / 2,
							this.mUlur.deepCopy(),
							this.getVertexBufferObjectManager());
					tombolAtas[i].setScale(Menu.player.scale / 2);
					arrow = tombolAtas[i];
					break;
				}
			}
			MoveXModifier mod = new MoveXModifier(actualDuration, -100,
					cameraWidth + 100);
			scene2.attachChild(arrow);
			arrow.registerEntityModifier(mod.deepCopy());
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Menu.menuActivity.finish();
			startActivity(new Intent(getApplicationContext(),
					Menu.class));
			finish();
			return super.onKeyDown(keyCode, event);
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
