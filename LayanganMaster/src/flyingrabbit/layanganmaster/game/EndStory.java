package flyingrabbit.layanganmaster.game;

import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.util.adt.io.in.IInputStreamOpener;



import java.io.IOException;
import java.io.InputStream;


import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;

import flyingrabbit.layanganmaster.helper.Player;

import android.content.Intent;
import android.view.Display;

public class EndStory extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mLayangan, mLove;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, love, layangan;
	Music music;
	
	private float bgPositionX;
	private float bgPositionY;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();

		final Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(cameraWidth, cameraHeight), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true);		
		
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("story/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sc18.png", 0,
						0);
		
		this.bgPositionX = cameraWidth / 2;
		this.bgPositionY = cameraHeight / 2;
		
		try {
			
			ITexture sc19 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc19.png");
						}
					});
			ITexture sc20 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc20.png");
						}
					});
			
			sc19.load();
			sc20.load();
			
			mLove = TextureRegionFactory.extractFromTexture(sc20);
			mLayangan = TextureRegionFactory.extractFromTexture(sc19);		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		 try
	        {
	            music = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this,"msc/end.mp3");
	            music.setLooping(true);
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
		
	}

	int i = 0;

	@Override
	protected Scene onCreateScene() {
		background = new
				Sprite(bgPositionX, bgPositionY,mBackgroundTextureRegion,getVertexBufferObjectManager());
		background.setScale(Menu.player.scale);
		
		scene.attachChild(background);
		
		if(Menu.player.isSound)	music.play();

		mEngine.registerUpdateHandler(new TimerHandler(3f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						if (i == 0) {
							layangan = new Sprite(bgPositionX, bgPositionY, mLayangan,
									getVertexBufferObjectManager());
							layangan.setScale(Menu.player.scale);
							scene.attachChild(layangan);
							i++;
						}else if(i == 1){
							 love = new Sprite(bgPositionX, bgPositionY, mLove,
									getVertexBufferObjectManager());
							 love.setScale(Menu.player.scale);
							scene.detachChild(layangan);
							 scene.attachChild(love);
							 i++;
						}else if(i == 2){
							i++;
						}else if(i == 3){
							i++;
							music.stop();
							mEngine.unregisterUpdateHandler(pTimerHandler);
							startActivity(new Intent(getApplicationContext(),
									Menu.class));
							finish();
						}
					}
				}));

		return scene;
	}

}

