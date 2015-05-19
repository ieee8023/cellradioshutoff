package the.radioshutoff;

import the.radioshutoff.R;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RadioShutoff extends Activity {

	Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio_shutoff);
		
		context = this;
		
		Button shutradiooff = (Button)findViewById(R.id.shutradiooff);
		
		shutradiooff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				changeRadioComponentEnabled(context, "cell", false, false);
				
			}
		});
		
		
		Button turnradioon = (Button)findViewById(R.id.turnradioon);
		
		turnradioon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				changeRadioComponentEnabled(context, "cell", true, false);
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_radio_shutoff, menu);
		return true;
	}

	
	
	
	
	/* Toggle airplane mode for 1 of the 4 allowed types
	 * type allowed values: cell, wifi, bluetooth, nfc
	 */
	private void changeRadioComponentEnabled(Context context, String type, boolean radio_component_enabled, boolean reset){     
	        // now toggle airplane mode from on to off, or vice versa
	        Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, radio_component_enabled ? 0 : 1);

	        
	        // now change system behavior so that only one component is turned off
	        // this also affects the future behavior of the system button for toggling air-plane mode. 
	        // to reset it in order to maintain the system behavior, set reset to true, otherwise we lazily make sure mobile voice is always on
	        Settings.System.putString(context.getContentResolver(), Settings.System.AIRPLANE_MODE_RADIOS, type); 

	        // post an intent to reload so the menu button switches to the new state we set
	        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
	        intent.putExtra("state", radio_component_enabled ? false : true);
	        context.sendBroadcast(intent);

	        // revert to default system behavior or not
	        if (reset){ Settings.System.putString(context.getContentResolver(), Settings.System.AIRPLANE_MODE_RADIOS, "cell,bluetooth,wifi,nfc"); }
	        // if reset to default is not chosen, always enable mobile cell at  least
	        // but only if NOT changing the mobile connection...
	        else if (type.indexOf("cell") == 0) { Settings.System.putString(context.getContentResolver(), Settings.System.AIRPLANE_MODE_RADIOS, "cell");}
	}//end method
	
}
