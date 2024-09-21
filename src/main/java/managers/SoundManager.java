package managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;



public class SoundManager {
	
    private static Map<String, Clip> sounds;

    static FloatControl gainControl;
    static boolean muted = false;
    
    public SoundManager() {
    	
    	String tag = new String("");
		sounds = new HashMap<>();

		gainControl = null;
	    try {
	    	
	    	tag = "hitSound";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "powerSmash";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "heavyHitSound";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "hitShield";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "elimination";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "endCharge";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "pause";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    
	    	tag = "menuSound";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "victory";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "gameOver";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
    	
	    	tag = "TempleMusic";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "CityMusic";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "SandMusic";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "mainMenu";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/"+ tag +".wav")));
	    	
	    	tag = "CustomMusic";
	    	sounds.put(tag, AudioSystem.getClip());
	    	sounds.get(tag).open(AudioSystem.getAudioInputStream(new File("resources/sounds/SandMusic.wav")));
	    	
	    	
	    } catch(Exception e) {
	    	System.out.print("[SoundManager] ERROR failed to load " + tag + " sounds");
	    }
	    
	    
    }
    
    public static void PlaySound(String tag) {


	    	if(tag.equals("SandMusic") || tag.equals("CityMusic") || tag.equals("TempleMusic") || tag.equals("mainMenu") || tag.equals("CustomMusic")) {
	    		gainControl = (FloatControl) sounds.get(tag).getControl(FloatControl.Type.MASTER_GAIN);
	    	    double gain = 0.60f;   
	    	    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
	    	    gainControl.setValue(dB);
	    	    
	    		sounds.get(tag).loop(Clip.LOOP_CONTINUOUSLY);
	    		sounds.get(tag).setFramePosition(1);
	    		if(muted)
	    			PauseSounds();
	    	}	
	    	else  {
	    		if(!muted) {
	    			
		    		gainControl = (FloatControl) sounds.get(tag).getControl(FloatControl.Type.MASTER_GAIN);
		    	    double gain = 0.30f;   
		    	    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
		    	    gainControl.setValue(dB);
		    		if(!sounds.get(tag).isActive())
		    			sounds.get(tag).setFramePosition(0);
			    	sounds.get(tag).start();
	    		}
		    }
//	    	if(!muted)
//	    		UnpauseSounds();
    }
    
    public static void StopSounds() {
    	for(Clip c: sounds.values()) {	
    		c.stop();
    		c.setFramePosition(0);
    	}	
    }
    
    public static void PauseSounds() {
    	for(Clip c: sounds.values()) {
    			c.stop();
    		
    	}	
    	muted = true;
    }
    
    public static void UnpauseSounds() {
    	for(Clip c: sounds.values()) {	
    		if(c.getFramePosition() != 0)
    			c.start();
    	}
    	muted = false;
    }
    public static boolean isPlaying(String tag) {
    	return sounds.get(tag).isActive();
    }
    public static void CloseSounds() {
    	for(Clip c: sounds.values())
    		c.close();
    }
}