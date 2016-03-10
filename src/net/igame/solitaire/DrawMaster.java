/*
  Copyright 2008 Google Inc.
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  Modified by Curtis Gedak 2015
 */
package net.igame.solitaire;

import net.igame.solitaire.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class DrawMaster {

	private Context mContext;

	// Background
	private int mScreenWidth;
	private int mScreenHeight;
	private int mDpi;
	private Paint mBGPaint;

	// Card stuff
	private final Paint mSuitPaint = new Paint();
	private Bitmap[] mCardBitmap;
	private Bitmap mCardHidden;
	private Bitmap mBackgroundBitmap;

	private Paint mEmptyAnchorPaint;
	private Paint mDoneEmptyAnchorPaint;
	private Paint mShadePaint;
	private Paint mLightShadePaint;

	private int roundEdge; // Round curve on each card corner
	private int cardOutline; // Card outline border thickness in pixels
	private int offset; // Whitespace between card border and font
	private Paint mGrnTxtPaint;
	private Paint mWhitePaint;
	private Paint mTimePaint;
	private Paint mMenuPaint;
	private int mLastSeconds;
	private String mTimeString;

	private Bitmap mBoardBitmap;
	private Canvas mBoardCanvas;

	private final Rect textBounds = new Rect(); // don't new this up in a draw
												// method

	public DrawMaster(Context context, int width, int height, int dpi) {

		mContext = context;
		mScreenWidth = width;
		mScreenHeight = height;
		mDpi = dpi;

		// Background
		mBGPaint = new Paint();
		mBGPaint.setARGB(255, 0, 128, 0);

		mShadePaint = new Paint();
		mShadePaint.setARGB(200, 0, 0, 0);

		mLightShadePaint = new Paint();
		mLightShadePaint.setARGB(100, 0, 0, 0);

		// Card related stuff
		mEmptyAnchorPaint = new Paint();
		mEmptyAnchorPaint.setARGB(255, 0, 64, 0);
		mDoneEmptyAnchorPaint = new Paint();
		mDoneEmptyAnchorPaint.setARGB(128, 255, 0, 0);
		roundEdge = 4 * mDpi / 160;
		cardOutline = 1 + (mDpi - 40) / 160;
		offset = mDpi / 160;

		mGrnTxtPaint = new Paint();
		mGrnTxtPaint.setARGB(255, 0, 128, 0);
		mGrnTxtPaint.setTextSize(18 * mDpi / 160);
		mGrnTxtPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF,
				Typeface.BOLD));
		mGrnTxtPaint.setTextAlign(Paint.Align.CENTER);
		mGrnTxtPaint.setAntiAlias(true);
		mWhitePaint = new Paint();
		mWhitePaint.setARGB(255, 255, 255, 255);
		mTimePaint = new Paint();
		mTimePaint.setTextSize(18 * mDpi / 160);
		mTimePaint.setTypeface(Typeface.create(Typeface.SANS_SERIF,
				Typeface.BOLD));
		mTimePaint.setTextAlign(Paint.Align.RIGHT);
		mTimePaint.setAntiAlias(true);
		mMenuPaint = new Paint();
		mMenuPaint.setTextSize(18 * mDpi / 160);
		mMenuPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF,
				Typeface.BOLD));
		mMenuPaint.setTextAlign(Paint.Align.CENTER);
		mMenuPaint.setAntiAlias(true);
		mLastSeconds = -1;

		mCardBitmap = new Bitmap[52];
		mBoardBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight,
				Bitmap.Config.RGB_565);
		mBoardCanvas = new Canvas(mBoardBitmap);
		
		
	}

	public int GetWidth() {
		return mScreenWidth;
	}

	public int GetHeight() {
		return mScreenHeight;
	}

	public Canvas GetBoardCanvas() {
		return mBoardCanvas;
	}

	public int GetDpi() {
		return mDpi;
	}

	public void DrawCard(Canvas canvas, Card card) {
		float x = card.GetX();
		float y = card.GetY();
		int idx = card.GetSuit() * 13 + (card.GetValue() - 1);
		canvas.drawBitmap(mCardBitmap[idx], x, y, mSuitPaint);
	}

	public void DrawHiddenCard(Canvas canvas, Card card) {
		float x = card.GetX();
		float y = card.GetY();
		canvas.drawBitmap(mCardHidden, x, y, mSuitPaint);
	}

	public void DrawCardCount(Canvas canvas, Card card, int count) {
		// Draw card count inside a white circle in lower right corner of card
		String twoDigits = "00";
		mGrnTxtPaint
				.getTextBounds(twoDigits, 0, twoDigits.length(), textBounds);
		float radius = textBounds.width() * 5 / 8;
		float x = card.GetX() + Card.WIDTH - radius - cardOutline * 2;
		float y = card.GetY() + Card.HEIGHT - radius + textBounds.height() / 2
				- cardOutline * 2;
		float cy = y + textBounds.exactCenterY();
		canvas.drawCircle(x, cy, radius, mWhitePaint);
		canvas.drawText(String.valueOf(count), x, y, mGrnTxtPaint);
	}

	public void DrawEmptyAnchor(Canvas canvas, float x, float y, boolean done) {
		RectF pos = new RectF(x, y, x + Card.WIDTH, y + Card.HEIGHT);
		if (!done) {
			canvas.drawRoundRect(pos, roundEdge, roundEdge, mEmptyAnchorPaint);
		} else {
			canvas.drawRoundRect(pos, roundEdge, roundEdge,
					mDoneEmptyAnchorPaint);
		}
	}

	public void DrawAnchorText(Canvas canvas, float x, float y, String text) {
		// Draw text, usually a single letter, in center of card anchor
		mGrnTxtPaint.getTextBounds(text, 0, text.length(), textBounds);
		int newX = (int) x + Card.WIDTH / 2;
		int newY = (int) y + (Card.HEIGHT + textBounds.height()) / 2;
		canvas.drawText(text, newX, newY, mGrnTxtPaint);
	}

	public void DrawBackground(Canvas canvas) {
		canvas.drawBitmap(mBackgroundBitmap, 0, 0, mBGPaint);
	}

	public void DrawShade(Canvas canvas) {
		canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, mShadePaint);
	}

	public void DrawLightShade(Canvas canvas) {
		canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, mLightShadePaint);
	}

	public void DrawLastBoard(Canvas canvas) {
		canvas.drawBitmap(mBoardBitmap, 0, 0, mSuitPaint);
	}

	public void SetScreenSize(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
		mBoardBitmap = Bitmap
				.createBitmap(width, height, Bitmap.Config.RGB_565);
		mBoardCanvas = new Canvas(mBoardBitmap);
	}

	public void DrawCards(boolean bigCards) {
		DrawBigCards(mContext.getResources());
		// if (bigCards) {
		//
		// } else {
		// // DrawCards(mContext.getResources());
		// }
	}

	private void DrawBigCards(Resources r) {
		Options options = new Options();
		options.inScaled = true;
		options.inDither = true;
		Log.e("Aaa", "cardWidth = " + Card.WIDTH);
		Log.e("aaa", "cardWidth = " + Card.HEIGHT);
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;		
		Bitmap tmp = BitmapFactory.decodeResource(r,
				R.drawable.card_back);
		
		mCardHidden = Bitmap.createScaledBitmap(tmp, Card.WIDTH,
				Card.HEIGHT, true);
		tmp = BitmapFactory.decodeResource(r,
				R.drawable.bg_main);
		mBackgroundBitmap = Bitmap.createScaledBitmap(tmp, mScreenWidth,
				mScreenHeight , true);
		for (int suitIdx = 0; suitIdx < 4; suitIdx++) {
			for (int valueIdx = 0; valueIdx < 13; valueIdx++) {
				int resId = r.getIdentifier(
						String.format("card_%s_%s", valueIdx + 1, suitIdx),
						"drawable", mContext.getPackageName());
				tmp = BitmapFactory.decodeResource(r, resId, options);
				mCardBitmap[suitIdx * 13 + valueIdx] = Bitmap
						.createScaledBitmap(tmp, Card.WIDTH, Card.HEIGHT, true);
				tmp = null;

			}
		}
	}

	public void DrawTime(Canvas canvas, int millis) {
		int seconds = (millis / 1000) % 60;
		int minutes = millis / 60000;
		if (seconds != mLastSeconds) {
			mLastSeconds = seconds;
			// String.format is insanely slow (~15ms)
			if (seconds < 10) {
				mTimeString = minutes + ":0" + seconds;
			} else {
				mTimeString = minutes + ":" + seconds;
			}
		}
		mTimePaint.setARGB(255, 20, 20, 20);
		canvas.drawText(mTimeString, mScreenWidth - 9, mScreenHeight - 9,
				mTimePaint);
		mTimePaint.setARGB(255, 0, 0, 0);
		canvas.drawText(mTimeString, mScreenWidth - 10, mScreenHeight - 10,
				mTimePaint);
	}

	public void DrawAltMenuString(Canvas canvas, String text) {
		mMenuPaint.setARGB(255, 20, 20, 20);
		canvas.drawText(text, canvas.getWidth() / 2, mScreenHeight - 9,
				mMenuPaint);
		mMenuPaint.setARGB(255, 0, 0, 0);
		canvas.drawText(text, canvas.getWidth() / 2 - 1, mScreenHeight - 10,
				mMenuPaint);
	}

	public void DrawRulesString(Canvas canvas, String score) {
		mTimePaint.setARGB(255, 20, 20, 20);
		canvas.drawText(score, mScreenWidth - 9, mScreenHeight
				- (18 * mDpi / 160) - 9, mTimePaint);
		if (score.charAt(0) == '-') {
			mTimePaint.setARGB(255, 255, 0, 0);
		} else {
			mTimePaint.setARGB(255, 0, 0, 0);
		}
		canvas.drawText(score, mScreenWidth - 10, mScreenHeight
				- (18 * mDpi / 160) - 10, mTimePaint);

	}
}
