package flyingrabbit.layanganmaster.game;

import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;


import java.io.IOException;
import java.io.InputStream;


import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import flyingrabbit.layanganmaster.helper.Player;

import android.app.Activity;
import android.content.Intent;
import android.view.Display;
import android.view.KeyEvent;

public class Menu extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mMain, mPengaturan, mMenu,
			mBantuan, mPiala, mBengkel, mHome, mSoundOn, mSoundOff, mCameraOn,
			mCameraOff, m10, m15, m30, md, mCoin, mDebt1, mMaster1, mLight1,
			mDebt2, mMaster2, mLight2, mK1, mK2, mK3, mSantaiButton,
			mEnerjikButton, mMenu2, mInfo, mCompass;

	int cameraWidth;
	int cameraHeight;

	boolean isHome = false;

	final Scene scene = new Scene();
	final Scene scene2 = new Scene();
	final Scene scene3 = new Scene();
	final Scene scene4 = new Scene();
	final Scene scene5 = new Scene();
	final Scene scene6 = new Scene();
	final Scene scene7 = new Scene();

	private BitmapTextureAtlas mBitmapTextureAtlas;

	Sprite background, main, pengaturan, menu, piala, bantuan, bengkel, home,
			soundOn, soundOff, cameraOn, cameraOff, harga1000, harga1500,
			harga3000, hd, coin, debt, master, light, k1, k2, k3, santaiButton,
			enerjikButton, menu2, home2, info, compass;

	static Music music;
	private Sound buttonClickedSound;
	private Sound itemBoughtSound;
	static Player player;
	float rachmad = 0.8f;

	private Font mFont, mStatusFont;	
	private Text text, attack, hp, exp, speed, defense, level;

	private float bgPositionX;
	private float bgPositionY;
	private float titlePositionX;
	private float titlePositionY;
	private float homePositionX;
	private float homePositionY;

	public static Activity menuActivity;

	@Override
	public EngineOptions onCreateEngineOptions() {
		menuActivity = this;
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		final Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						cameraWidth, cameraHeight), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);

		player = new Player(getApplicationContext());
		player.setScale(cameraWidth / 400f);
		player.setPaddingY((float) ((cameraHeight - 240 * player.scale) / 2));
		Debug.e("padding", "" + player.paddingY);

		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		menuResources();
		homePositionX = cameraWidth * 0.9f;
		homePositionY = 45 * player.scale / 2 + 10 * player.scale
				+ player.paddingY;
		try {
			music = MusicFactory
					.createMusicFromAsset(mEngine.getMusicManager(), this,
							"msc/background_music.ogg");
			music.setLooping(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		SoundFactory.setAssetBasePath("sfx/");
		try {
			this.buttonClickedSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "buttonclick.wav");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void menuResources() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("story/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sc1.png", 0,
						0);

		try {

			ITexture menu_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/menu.png");
						}
					});
			ITexture main_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/main.png");
						}
					});
			ITexture bantuan_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/help.png");
						}
					});
			ITexture pengaturan_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/setting.png");
						}
					});
			ITexture piala_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/piala.png");
						}
					});
			ITexture bengkel_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/bengkel.png");
						}
					});

			ITexture compass_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/tombolcompass.png");
						}
					});
			ITexture info_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/info.png");
						}
					});
			menu_asset.load();
			main_asset.load();
			piala_asset.load();
			pengaturan_asset.load();
			bantuan_asset.load();
			bengkel_asset.load();
			compass_asset.load();
			info_asset.load();

			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			mBantuan = TextureRegionFactory.extractFromTexture(bantuan_asset);
			mMain = TextureRegionFactory.extractFromTexture(main_asset);
			mPengaturan = TextureRegionFactory
					.extractFromTexture(pengaturan_asset);
			mPiala = TextureRegionFactory.extractFromTexture(piala_asset);
			mBengkel = TextureRegionFactory.extractFromTexture(bengkel_asset);
			mInfo = TextureRegionFactory.extractFromTexture(info_asset);
			mCompass = TextureRegionFactory.extractFromTexture(compass_asset);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

	}

	private void pilihResource() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png",
						0, 0);
		try {

			ITexture home_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});
			ITexture menu_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/pilih_layangan.png");
						}
					});

			ITexture asset_k1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite1.png");
						}
					});

			ITexture asset_k2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite2.png");
						}
					});

			ITexture asset_k3 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite3.png");
						}
					});

			menu_asset.load();
			home_asset.load();
			asset_k1.load();
			asset_k2.load();
			asset_k3.load();

			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			mK1 = TextureRegionFactory.extractFromTexture(asset_k1);
			mK2 = TextureRegionFactory.extractFromTexture(asset_k2);
			mK3 = TextureRegionFactory.extractFromTexture(asset_k3);

			ITexture menu_asset2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/pilih_mode.png");
						}
					});

			ITexture asset_santaibutton = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/santai.png");
						}
					});

			ITexture asset_enerjikbutton = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/enerjik.png");
						}
					});

			menu_asset2.load();
			asset_santaibutton.load();
			asset_enerjikbutton.load();
			mMenu2 = TextureRegionFactory.extractFromTexture(menu_asset2);
			mSantaiButton = TextureRegionFactory
					.extractFromTexture(asset_santaibutton);
			mEnerjikButton = TextureRegionFactory
					.extractFromTexture(asset_enerjikbutton);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
	}

	private void settingResource() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png",
						0, 0);
		try {

			ITexture soundOn_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/soundOn.png");
						}
					});
			ITexture soundOff_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/soundOff.png");
						}
					});
			ITexture cameraOn_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/cameraOn.png");
						}
					});
			ITexture cameraOff_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/cameraOff.png");
						}
					});
			ITexture home_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});
			ITexture menu_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets()
									.open("asset/pengaturan_menu.png");
						}
					});

			soundOff_asset.load();
			soundOn_asset.load();
			cameraOn_asset.load();
			cameraOff_asset.load();
			home_asset.load();
			menu_asset.load();

			mSoundOff = TextureRegionFactory.extractFromTexture(soundOff_asset);
			mSoundOn = TextureRegionFactory.extractFromTexture(soundOn_asset);
			mCameraOn = TextureRegionFactory.extractFromTexture(cameraOn_asset);
			mCameraOff = TextureRegionFactory
					.extractFromTexture(cameraOff_asset);
			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
	}

	private void bengkelResource() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png",
						0, 0);
		try {

			ITexture home_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});
			ITexture menu_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/bengkel_menu.png");
						}
					});

			ITexture asset_10 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/1000.png");
						}
					});

			ITexture asset_15 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/1500.png");
						}
					});

			ITexture asset_30 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/3000.png");
						}
					});

			ITexture asset_d = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kejual.png");
						}
					});
			ITexture asset_coin = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/coin2.png");
						}
					});

			menu_asset.load();
			home_asset.load();
			asset_10.load();
			asset_15.load();
			asset_30.load();
			asset_d.load();
			asset_coin.load();

			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			m10 = TextureRegionFactory.extractFromTexture(asset_10);
			m15 = TextureRegionFactory.extractFromTexture(asset_15);
			m30 = TextureRegionFactory.extractFromTexture(asset_30);
			md = TextureRegionFactory.extractFromTexture(asset_d);
			mCoin = TextureRegionFactory.extractFromTexture(asset_coin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "RAVIE.TTF", 20f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
		
		

		try {
			this.itemBoughtSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "buy_item.wav");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pialaResource() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png",
						0, 0);
		try {

			ITexture home_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});

			ITexture menu_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/piala_menu.png");
						}
					});

			ITexture debt1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/debt_no.png");
						}
					});
			ITexture debt2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/debt_ok.png");
						}
					});
			ITexture master1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lm_no.png");
						}
					});
			ITexture master2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lm_ok.png");
						}
					});
			ITexture lg1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lightning_no.png");
						}
					});
			ITexture lg2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lightning_ok.png");
						}
					});

			home_asset.load();
			menu_asset.load();
			debt1.load();
			debt2.load();
			master1.load();
			master2.load();
			lg1.load();
			lg2.load();

			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			mDebt1 = TextureRegionFactory.extractFromTexture(debt1);
			mDebt2 = TextureRegionFactory.extractFromTexture(debt2);
			mMaster1 = TextureRegionFactory.extractFromTexture(master1);
			mMaster2 = TextureRegionFactory.extractFromTexture(master2);
			mLight1 = TextureRegionFactory.extractFromTexture(lg1);
			mLight2 = TextureRegionFactory.extractFromTexture(lg2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

	}

	private void statusResource() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png",
						0, 0);
		try {

			ITexture home_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});
			ITexture menu_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/status_menu.png");
						}
					});

			menu_asset.load();
			home_asset.load();

			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "RAVIE.TTF", 20f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
		
		mStatusFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "Averia-Regular.ttf", 20f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mStatusFont.load();
	}

	private void loadPiala() {

		isHome = false;
		background = new Sprite(0, 0, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		background.setScale(player.scale);
		background.setPosition(this.bgPositionX, this.bgPositionY);
		scene5.attachChild(background);

		menu = new Sprite(0, 0, mMenu, getVertexBufferObjectManager());
		menu.setScale(player.scale);
		menu.setPosition(this.bgPositionX, this.bgPositionY);
		scene5.attachChild(menu);

		if (player.isDebt)
			debt = new Sprite(0, 0, mDebt2, getVertexBufferObjectManager());
		else
			debt = new Sprite(0, 0, mDebt1, getVertexBufferObjectManager());
		debt.setScale(player.scale);
		debt.setPosition(cameraWidth / 2f, player.getCameraHeight(0.7));
		scene5.attachChild(debt);

		if (player.isLightning)
			light = new Sprite(0, 0, mLight2, getVertexBufferObjectManager());
		else
			light = new Sprite(0, 0, mLight1, getVertexBufferObjectManager());
		light.setScale(player.scale);
		light.setPosition(cameraWidth / 2f, player.getCameraHeight(0.5));
		scene5.attachChild(light);

		if (player.isMaster)
			master = new Sprite(0, 0, mMaster2, getVertexBufferObjectManager());
		else
			master = new Sprite(0, 0, mMaster1, getVertexBufferObjectManager());
		master.setScale(player.scale);
		master.setPosition(cameraWidth / 2f, player.getCameraHeight(0.3));
		scene5.attachChild(master);

		home = new Sprite(0, 0, mHome, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					menuResources();
					scene5.clearChildScene();
					scene5.clearTouchAreas();
					loadMenu();
				}
				return true;
			};
		};

		home.setScale(player.scale);
		home.setPosition(this.homePositionX, this.homePositionY);

		scene5.attachChild(home);
		scene5.registerTouchArea(home);
		mEngine.setScene(scene5);
	}

	private void loadPilih() {
		isHome = false;
		background = new Sprite(this.bgPositionX, this.bgPositionY,
				mBackgroundTextureRegion, getVertexBufferObjectManager());
		background.setScale(player.scale);
		scene6.attachChild(background);
		menu = new Sprite(0, 0, mMenu, getVertexBufferObjectManager());

		menu.setScale(player.scale);
		menu.setPosition(this.bgPositionX, this.bgPositionY);
		scene6.attachChild(menu);

		k1 = new Sprite(0, 0, mK1, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					Debug.e("kite", "kite1");
					player.setKite(1);
					scene6.clearTouchAreas();
					scene6.detachChild(menu);
					scene6.detachChild(k1);
					scene6.detachChild(k2);
					scene6.detachChild(k3);
					scene6.detachChild(home);
					scene6.attachChild(menu2);
					scene6.attachChild(enerjikButton);
					scene6.attachChild(santaiButton);
					scene6.registerTouchArea(santaiButton);
					scene6.registerTouchArea(enerjikButton);
					scene6.registerTouchArea(home2);
					scene6.attachChild(home2);
				}
				return true;
			};
		};

		k1.setScale(player.scale);
		k1.setPosition(cameraWidth * 0.25f, player.getCameraHeight(0.5));
		scene6.attachChild(k1);
		scene6.registerTouchArea(k1);

		k2 = new Sprite(0, 0, mK2, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					Debug.e("kite", "kite2");
					player.setKite(2);
					scene6.clearTouchAreas();
					scene6.detachChild(menu);
					scene6.detachChild(k1);
					scene6.detachChild(k2);
					scene6.detachChild(k3);
					scene6.detachChild(home);
					scene6.attachChild(menu2);
					scene6.attachChild(enerjikButton);
					scene6.attachChild(santaiButton);
					scene6.registerTouchArea(santaiButton);
					scene6.registerTouchArea(enerjikButton);
					scene6.registerTouchArea(home2);
					scene6.attachChild(home2);
				}
				return true;
			};
		};

		k2.setScale(player.scale);
		k2.setPosition(cameraWidth * 0.45f, player.getCameraHeight(0.5));

		k3 = new Sprite(0, 0, mK3, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					Debug.e("kite", "kite3");
					player.setKite(3);
					scene6.clearTouchAreas();
					scene6.detachChild(menu);
					scene6.detachChild(k1);
					scene6.detachChild(k2);
					scene6.detachChild(k3);
					scene6.detachChild(home);
					scene6.attachChild(menu2);
					scene6.attachChild(enerjikButton);
					scene6.attachChild(santaiButton);
					scene6.registerTouchArea(santaiButton);
					scene6.registerTouchArea(enerjikButton);
					scene6.registerTouchArea(home2);
					scene6.attachChild(home2);

				}
				return true;
			};
		};

		k3.setScale(player.scale);
		k3.setPosition(cameraWidth * 0.7f, player.getCameraHeight(0.5));

		if (player.buyGreen) {
			scene6.attachChild(k3);
			scene6.registerTouchArea(k3);
		}

		if (player.buyPurple) {
			scene6.attachChild(k2);
			scene6.registerTouchArea(k2);
		}

		home = new Sprite(0, 0, mHome, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					menuResources();
					scene6.clearChildScene();
					scene6.clearTouchAreas();
					loadMenu();
				}
				return true;
			};
		};

		home.setScale(player.scale);
		home.setPosition(this.homePositionX, this.homePositionY);

		scene6.registerTouchArea(home);
		scene6.attachChild(home);

		home2 = new Sprite(0, 0, mHome, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					pilihResource();
					scene6.clearChildScene();
					scene6.clearTouchAreas();
					loadPilih();

				}
				return true;
			};
		};

		home2.setScale(player.scale);
		home2.setPosition(this.homePositionX, this.homePositionY);

		menu2 = new Sprite(0, 0, mMenu2, getVertexBufferObjectManager());
		menu2.setScale(player.scale);
		menu2.setPosition(this.bgPositionX, this.bgPositionY);

		santaiButton = new Sprite(0, 0, mSantaiButton,
				getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					player.setModeSantai(true);
					if (player.isSound) {
						music.pause();
					}
					startActivity(new Intent(getApplicationContext(),
							Lari.class));
					mEngine.registerUpdateHandler(new TimerHandler(0.5f, true,
							new ITimerCallback() {
								@Override
								public void onTimePassed(
										TimerHandler pTimerHandler) {

									mEngine.unregisterUpdateHandler(pTimerHandler);
									goToMenu();
								}
							}));
				}
				return true;
			};
		};

		santaiButton.setScale(player.scale);
		santaiButton.setPosition(cameraWidth / 2f, player.getCameraHeight(0.6));

		enerjikButton = new Sprite(0, 0, mEnerjikButton,
				getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					if (player.isSound) {
						music.pause();
					}
					player.setModeSantai(false);
					startActivity(new Intent(getApplicationContext(),
							Lari.class));
					mEngine.registerUpdateHandler(new TimerHandler(0.5f, true,
							new ITimerCallback() {
								@Override
								public void onTimePassed(
										TimerHandler pTimerHandler) {

									mEngine.unregisterUpdateHandler(pTimerHandler);
									goToMenu();
								}
							}));

				}
				return true;
			};
		};
		enerjikButton.setScale(player.scale);
		enerjikButton
				.setPosition(cameraWidth / 2f, player.getCameraHeight(0.4));

		mEngine.setScene(scene6);
	}

	private void loadSetting() {
		isHome = false;
		background = new Sprite(this.bgPositionX, this.bgPositionY,
				mBackgroundTextureRegion, getVertexBufferObjectManager());
		background.setScale(player.scale);
		scene2.attachChild(background);

		menu = new Sprite(0, 0, mMenu, getVertexBufferObjectManager());
		menu.setScale(player.scale);
		menu.setPosition(this.bgPositionX, this.bgPositionY);
		scene2.attachChild(menu);

		float sOnX, sOffX;
		if (player.isSound) {
			sOnX = cameraWidth * 0.3f;
			sOffX = -cameraWidth;
		} else {
			sOnX = -cameraWidth;
			sOffX = cameraWidth * 0.3f;
		}

		soundOn = new Sprite(0, 0, mSoundOn, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					if (player.isSound) {
						music.pause();
					}
					soundOn.setPosition(-cameraWidth,
							player.getCameraHeight(0.5));
					soundOff.setPosition(cameraWidth * 0.3f,
							player.getCameraHeight(0.5));
					player.setSound(0);
				}
				return true;
			};
		};
		soundOn.setScale(player.scale);
		soundOn.setPosition(sOnX, player.getCameraHeight(0.5));
		scene2.registerTouchArea(soundOn);
		scene2.attachChild(soundOn);

		soundOff = new Sprite(0, 0, mSoundOff, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					music.play();
					soundOff.setPosition(-cameraWidth,
							player.getCameraHeight(0.5));
					soundOn.setPosition(cameraWidth * 0.3f,
							player.getCameraHeight(0.5));
					player.setSound(1);
				}
				return true;
			};
		};

		soundOff.setScale(player.scale);
		soundOff.setPosition(sOffX, player.getCameraHeight(0.5));
		scene2.registerTouchArea(soundOff);
		scene2.attachChild(soundOff);

		float cOnX, cOffX;

		if (player.isCamera) {
			cOnX = cameraWidth * 0.7f;
			cOffX = -cameraWidth;
		} else {
			cOffX = cameraWidth * 0.7f;
			cOnX = -cameraWidth;
		}
		cameraOn = new Sprite(0, 0, mCameraOn, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					cameraOn.setPosition(-cameraWidth,
							player.getCameraHeight(0.5));
					cameraOff.setPosition(cameraWidth * 0.7f,
							player.getCameraHeight(0.5));
					player.setCamera(0);
				}
				return true;
			};
		};
		cameraOn.setScale(player.scale);
		cameraOn.setPosition(cOnX, player.getCameraHeight(0.5));

		scene2.registerTouchArea(cameraOn);
		scene2.attachChild(cameraOn);

		cameraOff = new Sprite(0, 0, mCameraOff, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					cameraOff.setPosition(-cameraWidth,
							player.getCameraHeight(0.5));
					cameraOn.setPosition(cameraWidth * 0.7f,
							player.getCameraHeight(0.5));
					player.setCamera(1);
				}
				return true;
			};
		};

		cameraOff.setScale(player.scale);

		cameraOff.setPosition(cOffX, player.getCameraHeight(0.5));
		scene2.registerTouchArea(cameraOff);
		scene2.attachChild(cameraOff);

		home = new Sprite(0, 0, mHome, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					menuResources();
					scene2.clearChildScene();
					scene2.clearTouchAreas();
					loadMenu();
				}
				return true;
			};
		};

		home.setScale(player.scale);
		home.setPosition(this.homePositionX, this.homePositionY);

		scene2.registerTouchArea(home);
		scene2.attachChild(home);
		mEngine.setScene(scene2);
	}

	private void loadStatus() {
		isHome = false;

		background = new Sprite(0, 0, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		background.setScale(player.scale);

		background.setPosition(this.bgPositionX, this.bgPositionY);
		scene4.attachChild(background);
		menu = new Sprite(0, 0, mMenu, getVertexBufferObjectManager());

		menu.setScale(player.scale);
		menu.setPosition(this.bgPositionX, this.bgPositionY);

		scene4.attachChild(menu);

		home = new Sprite(0, 0, mHome, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					menuResources();
					scene4.clearChildScene();
					scene4.clearTouchAreas();
					loadMenu();
				}
				return true;
			};
		};
		home.setScale(player.scale);
		home.setPosition(this.homePositionX, this.homePositionY);
		scene4.registerTouchArea(home);
		scene4.attachChild(home);

		hp = new Text(cameraWidth * 0.2f, player.getCameraHeight(0.7), mStatusFont,
				"HP     : " + player.HP, getVertexBufferObjectManager());
		hp.setScale(player.scale);
		scene4.attachChild(hp);

		exp = new Text(cameraWidth * 0.2f, player.getCameraHeight(0.5), mStatusFont,
				"EXP     : " + player.exp, getVertexBufferObjectManager());
		exp.setScale(player.scale);
		scene4.attachChild(exp);

		level = new Text(cameraWidth * 0.2f, player.getCameraHeight(0.3),
				mStatusFont, "LEVEL : " + player.level,
				getVertexBufferObjectManager());
		level.setScale(player.scale);
		scene4.attachChild(level);

		attack = new Text(cameraWidth * 0.7f, player.getCameraHeight(0.7),
				mStatusFont, "ATTACK  : " + player.attack,
				getVertexBufferObjectManager());
		attack.setScale(player.scale);
		scene4.attachChild(attack);

		defense = new Text(cameraWidth * 0.7f, player.getCameraHeight(0.5),
				mStatusFont, "DEFENSE : " + player.defense,
				getVertexBufferObjectManager());
		defense.setScale(player.scale);
		scene4.attachChild(defense);

		speed = new Text(cameraWidth * 0.7f, player.getCameraHeight(0.3),
				mStatusFont, "SPEED     : " + player.speed,
				getVertexBufferObjectManager());
		speed.setScale(player.scale);
		scene4.attachChild(speed);
		mEngine.setScene(scene4);
	}

	private void loadMenu() {
		isHome = true;

		background = new Sprite(0, 0, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		// background.setScaleCenter(0, 0);
		background.setScale(player.scale);

		this.bgPositionX = background.getWidthScaled() / 2;
		this.bgPositionY = background.getHeightScaled() / 2 + player.paddingY;

		background.setPosition(this.bgPositionX, this.bgPositionY);
		scene.attachChild(background);

		menu = new Sprite(0, 0, mMenu, getVertexBufferObjectManager());
		menu.setScale(player.scale);
		menu.setPosition(bgPositionX, bgPositionY);
		scene.attachChild(menu);

		main = new Sprite(0, 0, mMain, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					pilihResource();
					scene.clearChildScene();
					scene.clearTouchAreas();
					loadPilih();
				}
				return true;
			};
		};
		main.setScale(player.scale);

		float menuButtonPositionX = (float) (cameraWidth * 0.5);

		main.setPosition(menuButtonPositionX, (player.getCameraHeight(0.65)));

		scene.registerTouchArea(main);
		scene.attachChild(main);

		piala = new Sprite(0, 0, mPiala, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					pialaResource();
					scene.clearChildScene();
					scene.clearTouchAreas();
					loadPiala();
				}
				return true;
			};
		};
		piala.setScale(player.scale);
		piala.setPosition(menuButtonPositionX, player.getCameraHeight(0.4));
		scene.registerTouchArea(piala);
		scene.attachChild(piala);

		bengkel = new Sprite(0, 0, mBengkel, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					bengkelResource();
					scene.clearChildScene();
					scene.clearTouchAreas();
					loadBengkel();
				}
				return true;
			};
		};

		bengkel.setScale(player.scale);
		bengkel.setPosition(menuButtonPositionX, player.getCameraHeight(0.15));
		scene.registerTouchArea(bengkel);
		scene.attachChild(bengkel);

		compass = new Sprite(0, 0, mCompass, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					startActivity(new Intent(getApplicationContext(),
							Compass.class));
				}
				return true;
			};
		};
		compass.setScale(player.scale);
		compass.setPosition(cameraWidth * 0.15f, player.getCameraHeight(0.4));
		scene.registerTouchArea(compass);
		scene.attachChild(compass);

		pengaturan = new Sprite(0, 0, mPengaturan,
				getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					settingResource();
					scene.clearTouchAreas();
					scene.clearChildScene();
					loadSetting();

				}
				return true;
			};
		};

		pengaturan.setScale(player.scale);
		pengaturan
				.setPosition(cameraWidth * 0.85f, player.getCameraHeight(0.4));
		scene.registerTouchArea(pengaturan);
		scene.attachChild(pengaturan);

		bantuan = new Sprite(0, 0, mBantuan, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					statusResource();
					scene.clearTouchAreas();
					scene.clearChildScene();
					loadStatus();
				}
				return true;
			};
		};

		bantuan.setScale(player.scale);
		bantuan.setPosition(cameraWidth * 0.15f, player.getCameraHeight(0.15));
		scene.registerTouchArea(bantuan);
		scene.attachChild(bantuan);

		info = new Sprite(0, 0, mInfo, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					instructionResource();
					scene.clearTouchAreas();
					scene.clearChildScene();
//					startActivity(new Intent(getApplicationContext(),
//							EndStory.class));
//					finish();
					loadInstruction();

				}
				return true;
			};
		};
		info.setScale(player.scale);
		info.setPosition(cameraWidth * 0.85f, player.getCameraHeight(0.15));
		scene.registerTouchArea(info);
		scene.attachChild(info);

		mEngine.registerUpdateHandler(new TimerHandler(1f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);

						if (player.isSound) {
							music.play();
						}

					}
				}));
		mEngine.setScene(scene);
	}
	
	private void instructionResource(){
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "petunjuk.png",
						0, 0);
		try {

			ITexture home_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});
						
			home_asset.load();

			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
	}
	
	private void loadInstruction(){
		isHome = false;
		
		background = new Sprite(0, 0, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		background.setScale(player.scale);

		background.setPosition(this.bgPositionX, this.bgPositionY);
		scene7.attachChild(background);
		
		
		home = new Sprite(0, 0, mHome, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					menuResources();
					scene7.clearChildScene();
					scene7.clearTouchAreas();
					loadMenu();
				}
				return true;
			};
		};
		home.setScale(player.scale);
		home.setPosition(this.homePositionX + 40, this.homePositionY - 30);
		scene7.registerTouchArea(home);
		scene7.attachChild(home);
		mEngine.setScene(scene7);
		
	}

	private void loadBengkel() {

		background = new Sprite(0, 0, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		background.setScale(player.scale);
		background.setPosition(this.bgPositionX, this.bgPositionY);

		scene3.attachChild(background);

		menu = new Sprite(0, 0, mMenu, getVertexBufferObjectManager());

		menu.setScale(player.scale);
		menu.setPosition(menu.getWidthScaled() / 2,
				cameraHeight - (menu.getHeightScaled() / 2 + player.paddingY));
		scene3.attachChild(menu);

		if (player.buyBlue) {
			harga1000 = new Sprite(0, 0, md, getVertexBufferObjectManager());
		} else {
			harga1000 = new Sprite(0, 0, m10, getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float X, float Y) {
					if (pSceneTouchEvent.isActionDown()) {
						if (player.coin >= 1000) {
							if (player.isSound) {
								itemBoughtSound.play();
							}
						}
					}
					if (pSceneTouchEvent.isActionUp()) {
						if (player.coin >= 1000) {
							player.updateCoin(player.coin - 1000);
							text.setText(player.coin + "");
							player.beli("blue");
							scene3.unregisterTouchArea(harga1000);
							scene3.detachChild(harga1000);
							harga1000 = new Sprite(0, 0, md,
									getVertexBufferObjectManager());
							harga1000.setScale(player.scale);
							harga1000.setPosition(cameraWidth * 0.55f
									+ harga1000.getWidthScaled() / 2,
									player.getCameraHeight(0.315));
							scene3.attachChild(harga1000);
						}
					}
					return true;
				};
			};
		}
		harga1000.setScale(player.scale);
		harga1000.setPosition(cameraWidth * 0.55f + harga1000.getWidthScaled()
				/ 2, player.getCameraHeight(0.315));
		scene3.attachChild(harga1000);
		scene3.registerTouchArea(harga1000);

		if (player.buyPurple) {
			harga1500 = new Sprite(0, 0, md, getVertexBufferObjectManager());
		} else {
			harga1500 = new Sprite(0, 0, m15, getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float X, float Y) {
					if (pSceneTouchEvent.isActionDown()) {
						if (player.coin >= 1500) {
							if (player.isSound) {
								itemBoughtSound.play();
							}
						}
					}
					if (pSceneTouchEvent.isActionUp()) {
						if (player.coin >= 1500) {
							player.updateCoin(player.coin - 1500);
							text.setText(player.coin + "");
							player.beli("purple");
							scene3.unregisterTouchArea(harga1500);
							scene3.detachChild(harga1500);
							harga1500 = new Sprite(0, 0, md,
									getVertexBufferObjectManager());
							harga1500.setScale(player.scale);
							harga1500.setPosition(cameraWidth * 0.55f
									+ harga1500.getWidthScaled() / 2,
									player.getCameraHeight(0.725));
							scene3.attachChild(harga1500);

						}
					}
					return true;
				};
			};
		}

		harga1500.setScale(player.scale);
		harga1500.setPosition(cameraWidth * 0.55f + harga1500.getWidthScaled()
				/ 2, player.getCameraHeight(0.7255));
		scene3.registerTouchArea(harga1500);
		scene3.attachChild(harga1500);

		if (player.buyGreen) {
			harga3000 = new Sprite(0, 0, md, getVertexBufferObjectManager());
		} else {
			harga3000 = new Sprite(0, 0, m30, getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float X, float Y) {
					if (pSceneTouchEvent.isActionDown()) {
						if (player.coin >= 3000) {
							if (player.isSound) {
								itemBoughtSound.play();
							}
						}
					}
					if (pSceneTouchEvent.isActionUp()) {
						if (player.coin >= 3000) {
							player.updateCoin(player.coin - 3000);
							text.setText(player.coin + "");
							player.beli("green");
							scene3.unregisterTouchArea(harga3000);
							scene3.detachChild(harga3000);
							harga3000 = new Sprite(0, 0, md,
									getVertexBufferObjectManager());
							harga3000.setScale(player.scale);
							harga3000.setPosition(cameraWidth * 0.55f
									+ harga3000.getWidthScaled() / 2,
									player.getCameraHeight(0.5));
							scene3.attachChild(harga3000);
						}
					}
					return true;
				};
			};
		}

		harga3000.setScale(player.scale);
		harga3000.setPosition(cameraWidth * 0.55f + harga3000.getWidthScaled()
				/ 2, player.getCameraHeight(0.5));
		scene3.registerTouchArea(harga3000);
		scene3.attachChild(harga3000);

		home = new Sprite(0, 0, mHome, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (player.isSound) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					menuResources();
					scene3.clearChildScene();
					scene3.clearTouchAreas();
					itemBoughtSound = null;
					loadMenu();
				}
				return true;
			};
		};

		home.setScale(player.scale);
		home.setPosition(this.homePositionX, this.homePositionY);
		scene3.registerTouchArea(home);
		scene3.attachChild(home);

		coin = new Sprite(0, 0, mCoin, getVertexBufferObjectManager());
		coin.setScale(player.scale);
		coin.setPosition(cameraWidth * 0.72f,
				player.getCameraHeight(1) - coin.getHeightScaled());
		scene3.attachChild(coin);
		text = new Text(0, 0, mFont, player.coin + "",
				getVertexBufferObjectManager());
		text.setScale(player.scale);
		text.setPosition(
				coin.getX() + coin.getWidthScaled() + text.getWidthScaled() / 2,
				player.getCameraHeight(1) - coin.getHeightScaled());
		scene3.attachChild(text);

		mEngine.setScene(scene3);
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		loadMenu();
		return scene;
	}

	public void goToMenu() {
		scene2.clearTouchAreas();
		scene3.clearTouchAreas();
		scene4.clearTouchAreas();
		scene5.clearTouchAreas();
		scene6.clearTouchAreas();
		scene2.clearChildScene();
		scene3.clearChildScene();
		scene4.clearChildScene();
		scene5.clearChildScene();
		scene6.clearChildScene();
		menuResources();
		loadMenu();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (isHome) {
				if (this.isGameLoaded()) {
					mEngine.onDestroy();
					System.exit(0);
				}
				return super.onKeyDown(keyCode, event);
			} else {
				goToMenu();
				return true;
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
