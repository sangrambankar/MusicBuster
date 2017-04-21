package com.blakky.musicbuster.helpers;

import java.util.Formatter;

public class TimeHelper {
	private int hours;
	private int minutes;
	private int seconds;
	
	public TimeHelper(int milliseconds){
		this.calculate(milliseconds);
	}

	/**
	 * Converts milliseconds to hours, minutes and seconds.
	 * @param milliseconds The value to be converted.
     */
	private void calculate(int milliseconds){
		milliseconds /= 1000;
		this.hours = milliseconds / 3600;
		this.minutes = ( milliseconds - ( 3600 * this.hours ) ) / 60;
		this.seconds = milliseconds - ( ( this.hours * 3600 ) + ( this.minutes * 60 ) );
	}


	public int getHours() {
		return hours;
	}

	private int getMinutes() {
		return minutes;
	}

	private int getSeconds() {
		return seconds;
	}
	
	public void setHours(int hours) {
		this.hours = hours;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	@SuppressWarnings("resource")
	@Override
	public String toString() {
		return  new Formatter().format("%02d",this.getMinutes()) + ":"
				+ new Formatter().format("%02d",this.getSeconds());
	}	
	
}
