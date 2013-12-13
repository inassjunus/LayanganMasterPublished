package flyingrabbit.layanganmaster.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.sensor.location.ILocationListener;
import org.andengine.input.sensor.location.LocationProviderStatus;
import org.andengine.input.sensor.location.LocationSensorOptions;
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

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;

public class Lari extends SimpleBaseGameActivity implements
		SensorEventListener, ILocationListener {

	private ITextureRegion mBackgroundTextureRegion, mStart, mStartSantai, mFinish;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, finish, start, startSantai;

	float rachmad = 0.8f;
	private Font mFont;
	private Text count, direction;
	private int tempX = 0;
	private int tempY = 0;
	private int accellerometerSpeedX;
	private int accellerometerSpeedY;
	private int pedo;
	private SensorManager sensorManager;
	private Location mUserLocation;
	private float latitude_start, longitude_start;
	private float latitude_finish, longitude_finish;
	boolean counterON = false;
	private static final double RADIUS_EARTH_METERS = 6371000;

	private Sound buttonClickedSound;
	private Sound beep;
	private boolean isSoundOn = false;
	
	private boolean isSantai = false;
	
	int ii = 5;

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		final Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						cameraWidth, cameraHeight), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		isSantai = Menu.player.isSantai;
		this.mUserLocation = new Location(LocationManager.GPS_PROVIDER);
		
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		sensorManager = (SensorManager) this
				.getSystemService(this.SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_GAME);

		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				updateSpritePosition();
			}

			public void reset() {
				// TODO Auto-generated method stub
			}
		});

		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png",
						0, 0);

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "RAVIE.TTF", 40f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();

		try {
			ITexture start_asset;
			if(!this.isSantai) {
				start_asset = new BitmapTexture(this.getTextureManager(),
						new IInputStreamOpener() {
							@Override
							public InputStream open() throws IOException {
								return getAssets().open("asset/lari.png");
							}
						});
			} else {
				start_asset = new BitmapTexture(this.getTextureManager(),
						new IInputStreamOpener() {
							@Override
							public InputStream open() throws IOException {
								return getAssets().open("asset/kocok.png");
							}
						});
			}
			
			
			ITexture finish_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/terbangkan.png");
						}
					});

			start_asset.load();
			finish_asset.load();;

			mStart = TextureRegionFactory.extractFromTexture(start_asset);
			mFinish = TextureRegionFactory.extractFromTexture(finish_asset);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

		isSoundOn = Menu.player.isSound;

		if (isSoundOn) {
			SoundFactory.setAssetBasePath("sfx/");
			try {
				this.buttonClickedSound = SoundFactory.createSoundFromAsset(
						mEngine.getSoundManager(), this, "buttonclick.wav");
				beep = SoundFactory.createSoundFromAsset(
						mEngine.getSoundManager(), this, "beep.ogg");
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected Scene onCreateScene() {

		background = new Sprite(0, 0, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		background.setScale(Menu.player.scale);

		float bgPositionX = background.getWidthScaled() / 2;
		float bgPositionY = background.getHeightScaled() / 2
				+ Menu.player.paddingY;
		background.setPosition(bgPositionX, bgPositionY);
		scene.attachChild(background);

		start = new Sprite(0, 0, mStart, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					if (isSoundOn) {
						buttonClickedSound.play();
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					start.setAlpha(0f);
					if (!Menu.player.isSantai) {
						scene.detachChild(direction);
						latitude_start = (float) mUserLocation.getLatitude();
						longitude_start = (float) mUserLocation.getLongitude();
					}
					counterON = true;
					mEngine.registerUpdateHandler(new TimerHandler(1f, true,
							new ITimerCallback() {
								@Override
								public void onTimePassed(
										TimerHandler pTimerHandler) {
									
									if (ii >= 0) {
										if (isSoundOn) {
											beep.play();
										}
										scene.unregisterTouchArea(start);
										count.setText("" + ii--);
									} else {
										counterON = false;
										if (!Menu.player.isSantai) {
											latitude_finish = (float) mUserLocation
													.getLatitude();
											longitude_finish = (float) mUserLocation
													.getLongitude();
										}
										mEngine.unregisterUpdateHandler(pTimerHandler);
										scene.detachChild(count);
										scene.detachChild(start);
										scene.attachChild(finish);
										scene.registerTouchArea(finish);

									}
								}
							}));

				}
				return true;
			};
		};

		start.setScale(Menu.player.scale);

		finish = new Sprite(0, 0, mFinish, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					scene.clearTouchAreas();
					if (isSoundOn) {
						buttonClickedSound.play();
					}
					scene.detachChild(finish);
					Menu.player.lari = getVelocity();
					if (Menu.player.isCamera) {
						startActivity(new Intent(
								getApplicationContext(),
								AugmentedActivity.class));
						finish();
					} else {
						startActivity(new Intent(
								getApplicationContext(),
								NonAugmentedActivity.class));
						finish();
					}					
				}
				
				return true;
			};
		};

		finish.setScale(Menu.player.scale);
		finish.setPosition(cameraWidth * 0.5f, Menu.player.getCameraHeight(0.5));

		count = new Text(0, 0, mFont, "              ", getVertexBufferObjectManager());
		count.setScale(Menu.player.scale * 2);
		count.setPosition(cameraWidth / 2, Menu.player.getCameraHeight(0.5));
		scene.attachChild(count);

		start.setPosition(cameraWidth * 0.5f, Menu.player.getCameraHeight(0.5));

		if (!Menu.player.isSantai) {
			direction = new Text(0, 0, mFont, "" + generateDirection(),
					getVertexBufferObjectManager());
			direction.setScale(Menu.player.scale);
			direction.setPosition(cameraWidth * 0.5f,
					Menu.player.getCameraHeight(0.7));
			start.setPosition(cameraWidth * 0.5f,
					Menu.player.getCameraHeight(0.4));
			scene.attachChild(direction);
			ii = 180;
		}

		scene.attachChild(start);
		scene.registerTouchArea(start);
		return scene;
	}

	int dir = 1;

	private String generateDirection() {
		String directionName = "utara";
		Random rand = new Random();
		float temp = rand.nextFloat();
		if (temp < 0.25) {
			dir = 2;
			directionName = "barat";
		} else if (temp < 0.5) {
			dir = 4;
			directionName = "timur";
		} else if (temp < 0.25) {
			dir = 3;
			directionName = "selatan";
		}
		return directionName;
	}

	protected void updateSpritePosition() {
		// TODO Auto-generated method stub
		if (counterON) {
			if (Math.abs(accellerometerSpeedX - tempX) > 3
					|| Math.abs(accellerometerSpeedY - tempY) > 3) {
				pedo++;
			}
		}
		tempX = accellerometerSpeedX;
		tempY = accellerometerSpeedY;

	}

	@Override
	public void onLocationProviderEnabled() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location pLocation) {
		// TODO Auto-generated method stub
		this.mUserLocation = pLocation;
	}

	@Override
	public void onLocationLost() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationProviderDisabled() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationProviderStatusChanged(
			LocationProviderStatus pLocationProviderStatus, Bundle pBundle) {
		// TODO Auto-generated method stub

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

	@Override
	protected void onResume() {
		if (mEngine != null) {
			super.onResume();

			final LocationSensorOptions locationSensorOptions = new LocationSensorOptions();
			locationSensorOptions.setAccuracy(Criteria.ACCURACY_COARSE);
			locationSensorOptions.setMinimumTriggerTime(0);
			locationSensorOptions.setMinimumTriggerDistance(0);
			this.enableLocationSensor(this, locationSensorOptions);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		this.mEngine.disableOrientationSensor(this);
		this.mEngine.disableLocationSensor(this);
	}

	private double getVelocity() {
		double result = 0;
		if (!Menu.player.isSantai) {
			if (dir == 1) {
				if (latitude_finish - latitude_start > 0) {
					result = Math.acos(Math.sin(latitude_start)
							* Math.sin(latitude_finish)
							+ Math.cos(latitude_start)
							* Math.cos(latitude_finish)
							* Math.cos(longitude_start - longitude_start))
							* RADIUS_EARTH_METERS;
				}
			} else if (dir == 2) {
				if (latitude_start - longitude_finish > 0) {
					result = Math.acos(Math.sin(latitude_start)
							* Math.sin(latitude_start)
							+ Math.cos(latitude_start)
							* Math.cos(latitude_start)
							* Math.cos(longitude_finish - longitude_start))
							* RADIUS_EARTH_METERS;
				}
			} else if (dir == 3) {
				if (latitude_finish - latitude_start < 0) {
					result = Math.acos(Math.sin(latitude_start)
							* Math.sin(latitude_finish)
							+ Math.cos(latitude_start)
							* Math.cos(latitude_finish)
							* Math.cos(longitude_start - longitude_start))
							* RADIUS_EARTH_METERS;
				}
			} else if (dir == 4) {
				if (latitude_start - longitude_finish < 0) {
					result = Math.acos(Math.sin(latitude_start)
							* Math.sin(latitude_start)
							+ Math.cos(latitude_start)
							* Math.cos(latitude_start)
							* Math.cos(longitude_finish - longitude_start))
							* RADIUS_EARTH_METERS;
				}
			}
			return (result * 2 + pedo) / 180;
		} else {
			return pedo / 5;
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

		}
		return super.onKeyDown(keyCode, event);
	}
}
