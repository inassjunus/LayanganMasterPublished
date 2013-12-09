package flyingrabbit.layanganmaster.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import android.view.Display;

public class BattleMode extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mHitArea, mCollidePoint,
			mTarik, mUlur, mVs1, mVs2, mVs3;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, hitArea1, collidePoint1, hitArea2, collidePoint2,
			buttonUp, buttonDown, tarik, ulur, vs;
	private Font mFont;
	private Text attackCountText;
	private int attackCount = 0;
	
	private Sprite[] tombolBawah = new Sprite[20];
	private boolean[] isActive = new boolean[20];
	int collide = -1;

	private Sprite[] tombolAtas = new Sprite[20];
	private boolean[] isActive2 = new boolean[20];
	int collide2 = -1;

	private Camera camera;
	private float startingDuration = 8.0f;
	
	int hit = 0, miss = 0;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						cameraWidth, cameraHeight), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sky.jpg", 0,
						0);

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "RAVIE.TTF", 40f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();

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

			bg_asset.load();
			collidepoint_asset.load();
			tarik_asset.load();
			ulur_asset.load();
			vs1_asset.load();
			vs2_asset.load();
			vs3_asset.load();

			mHitArea = TextureRegionFactory.extractFromTexture(bg_asset);
			mCollidePoint = TextureRegionFactory
					.extractFromTexture(collidepoint_asset);
			mTarik = TextureRegionFactory.extractFromTexture(tarik_asset);
			mUlur = TextureRegionFactory.extractFromTexture(ulur_asset);
			mVs1 = TextureRegionFactory.extractFromTexture(vs1_asset);
			mVs2 = TextureRegionFactory.extractFromTexture(vs2_asset);
			mVs3 = TextureRegionFactory.extractFromTexture(vs3_asset);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected Scene onCreateScene() {

		background = new Sprite(
				mBackgroundTextureRegion.getWidth() / 2,
				mBackgroundTextureRegion.getHeight() / 2 + Menu.player.paddingY,
				mBackgroundTextureRegion, getVertexBufferObjectManager());

		this.mEngine.registerUpdateHandler(new FPSLogger());
		background.setScale(Menu.player.scale);
		background.setScaleCenter(0, 0);
		scene.attachChild(background);

		hitArea1 = new Sprite(0, 0, mHitArea, getVertexBufferObjectManager());
		hitArea1.setScale(Menu.player.scale);
		hitArea1.setPosition(cameraWidth / 2,
				cameraHeight - hitArea1.getHeightScaled() / 2
						- Menu.player.paddingY);
		scene.attachChild(hitArea1);

		collidePoint1 = new Sprite(0, 0, mCollidePoint,
				getVertexBufferObjectManager());
		collidePoint1.setScale(Menu.player.scale);
		collidePoint1.setPosition(cameraWidth - collidePoint1.getWidthScaled()
				/ 2, cameraHeight - collidePoint1.getHeightScaled() / 2
				- Menu.player.paddingY);
		scene.attachChild(collidePoint1);

		hitArea2 = new Sprite(0, 0, mHitArea, getVertexBufferObjectManager());
		hitArea2.setScale(Menu.player.scale);
		hitArea2.setPosition(
				cameraWidth / 2,
				cameraHeight - hitArea2.getHeightScaled() / 2
						- Menu.player.getCameraHeight(0.15));
		scene.attachChild(hitArea2);

		collidePoint2 = new Sprite(0, 0, mCollidePoint,
				getVertexBufferObjectManager());
		collidePoint2.setScale(Menu.player.scale);
		collidePoint2.setPosition(cameraWidth - collidePoint2.getWidthScaled()
				/ 2, cameraHeight - collidePoint2.getHeightScaled() / 2
				- Menu.player.getCameraHeight(0.15));
		scene.attachChild(collidePoint2);

		ulur = new Sprite(0, 0, mUlur, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					if (collide2 >= 0) {
						hit++;
						scene.detachChild(tombolAtas[collide2]);
						tombolAtas[collide2] = null;
						isActive2[collide2] = false;
						collide2 = -1;
					} else {
						miss++;
					}
				}
				return true;
			};
		};

		ulur.setScale(Menu.player.scale);
		ulur.setPosition(cameraWidth - ulur.getWidthScaled() / 2,
				ulur.getHeightScaled() / 2 + Menu.player.paddingY);
		scene.attachChild(ulur);
		scene.registerTouchArea(ulur);

		tarik = new Sprite(0, 0, mTarik, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {

				if (pSceneTouchEvent.isActionUp()) {
					if (collide >= 0) {
						hit++;
						scene.detachChild(tombolBawah[collide]);
						tombolBawah[collide] = null;
						isActive[collide] = false;
						collide = -1;
					} else {
						miss++;
					}
				}

				return true;
			};
		};

		tarik.setScale(Menu.player.scale);
		tarik.setPosition(tarik.getWidthScaled() / 2, tarik.getHeightScaled()
				/ 2 + Menu.player.paddingY);
		scene.attachChild(tarik);
		scene.registerTouchArea(tarik);
		//
		attackCountText = new Text(cameraWidth / 2, 0, mFont, "           ",
				getVertexBufferObjectManager());
		attackCountText.setScaleCenter(0, 0);
		attackCountText.setScale(Menu.player.scale);
		scene.attachChild(attackCountText);
		//

		this.createSpriteSpawnTimeHandler();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						// TODO Auto-generated method stub

					
						collide = -1;
						for (int i = 0; i < 20; i++) {
							if (tombolBawah[i] != null) {
								if (tombolBawah[i].getX() > cameraWidth+50) {
									scene.detachChild(tombolBawah[i]);
									tombolBawah[i] = null;
									isActive[i] = false;
									miss++;
								} else if (tombolBawah[i]
										.collidesWith(collidePoint2)) {
									collide = i;
									break;
								}
							}
						}
						
						collide2 = -1;
						for (int i = 0; i < 20; i++) {
							if (tombolAtas[i] != null) {
								if (tombolAtas[i].getX() > cameraWidth+50) {
									scene.detachChild(tombolAtas[i]);
									tombolAtas[i] = null;
									isActive2[i] = false;
									miss++;
								} else if (tombolAtas[i]
										.collidesWith(collidePoint1)) {
									collide2 = i;
									break;
								}
							}
						}

					}
				}));
		vs = new Sprite(0, 0, mVs1, getVertexBufferObjectManager());
		vs.setScale(Menu.player.scale);
		vs.setPosition(cameraWidth / 2, cameraHeight / 2);
		scene.attachChild(vs);

		return scene;
	}

	public void addArrow() {
		Random rand = new Random();
		float tempArrow = rand.nextFloat();
		float actualDuration = startingDuration;
		if(hit>=10){
			hit = 0;
			if(startingDuration > 2f)
			startingDuration -= 0.7f;
		}
		
		if(miss >= 5){
			miss = 0;
			if(startingDuration < 8f)
			startingDuration += 0.7f;
		}

		Sprite arrow = null;
		if (tempArrow <= 0.5) {
			for (int i = 0; i < 20; i++) {
				if (!isActive[i]) {
					isActive[i] = true;
					tombolBawah[i] = new Sprite(-100, cameraHeight
							- hitArea2.getHeightScaled() / 2
							- Menu.player.getCameraHeight(0.15),
							this.mTarik.deepCopy(),
							this.getVertexBufferObjectManager());
					tombolBawah[i].setScale(Menu.player.scale / 2);
					arrow = tombolBawah[i];
					break;
				}
			}
			
			MoveXModifier mod = new MoveXModifier(actualDuration, -100,
					cameraWidth + 100);
			scene.attachChild(arrow);
			arrow.registerEntityModifier(mod.deepCopy());
		} else {
			for (int i = 0; i < 20; i++) {
				if (!isActive2[i]) {
					isActive2[i] = true;
					tombolAtas[i] = new Sprite(-100, cameraHeight
							- hitArea1.getHeightScaled() / 2
							- Menu.player.paddingY, this.mUlur.deepCopy(),
							this.getVertexBufferObjectManager());
					tombolAtas[i].setScale(Menu.player.scale / 2);
					arrow = tombolAtas[i];
					break;
				}
			}
			MoveXModifier mod = new MoveXModifier(actualDuration, -100,
					cameraWidth + 100);
			scene.attachChild(arrow);
			arrow.registerEntityModifier(mod.deepCopy());
		}

		

	}

	private void createSpriteSpawnTimeHandler() {
		TimerHandler spriteTimerHandler;
		float mEffectSpawnDelay = 1f;

		spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						addArrow();
					}
				});

		getEngine().registerUpdateHandler(spriteTimerHandler);
	}

}