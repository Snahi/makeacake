package com.snavi.makecake.cooking;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.snavi.makecake.Activities.MixActivity;
import com.snavi.makecake.R;
import com.snavi.makecake.sensorListeners.ProximitySensorListener;

import java.util.ArrayList;


public class ChooseIngredientsView extends SurfaceView implements SurfaceHolder.Callback {


    // CONST //////////////////////////////////////////////////////////////////////////////////////
    private static final int INGREDIENT_SIZE        = 500;
    private static final int BAD_CHOICE_DISPLAY_TIME         = 400;
    private static final int TIME_BETWEEN_INGREDIENT_CHANGES = 1000;
    private static final int COLLECTED_INGREDIENT_SIZE       = 300;
    private static final int COLLECTED_BACKGROUND_OPACITY    = 100;
    private static final int COLLECTED_BACKGROUND_COLOR      = Color.BLUE;
    private static final int COLLECTED_BACKGROUND_MARGIN     = 20;
    private static final int COLLECTED_BACKGROUND_CORNER     = 30;
    private static final int VIBRATION_TIME_WHEN_BAD_INGREDIENT = 500;
    private static final double NEW_INGREDIENT_ALL_PERCENTAGE   = 0.7;

    public static final int[] ALL_INGREDIENTS =
            {
                    R.drawable.apple,
                    R.drawable.blueberry,
                    R.drawable.cherry,
                    R.drawable.chocolate,
                    R.drawable.egg,
                    R.drawable.flour,
                    R.drawable.hazelnut,
                    R.drawable.pinapple,
                    R.drawable.raspberry,
                    R.drawable.strawberry,
                    R.drawable.sugar,
                    R.drawable.walnut,
                    R.drawable.watermelon
            };


    // fields /////////////////////////////////////////////////////////////////////////////////////
    private GameThread m_gameThread;
    private Context    m_context;

    private int m_screenWidth;
    private int m_screenHeight;

    private ArrayList<Integer> m_requiredIngredientsImages;    // list of ingredients, that player must collect
    private ArrayList<Integer> m_mixImages;                    // images for mix animation for current cake

    private int m_finalImg;                                     // ready cake image

    private Paint m_ingredientPaint;
    private Paint m_collectedBackgroundPaint;

    private Vibrator m_vibrator;
    private ProximitySensorListener m_proximitySensorListener;

    // images //////////////////////////////////////////////////////////////////////////////////////
    private android.graphics.Bitmap m_kitchen;
    private android.graphics.Bitmap m_kitchenRed;


    // game state //////////////////////////////////////////////////////////////////////////////////
    private int m_ingredientX;
    private int m_ingredientY;
    private int m_currIngredientIdx;
    private long m_lastIngredientChange;
    private volatile ArrayList<Integer> m_collectedIngredients;
    private boolean m_badChoice;                                    // true -> player selected bad ingredient
    private long m_displayBadStart;                                 // first time when kitchen_red wast displayed


    public ChooseIngredientsView(Context context, ArrayList<Integer> ingredientsImages,
                                 ArrayList<Integer> mixImages, int screenWidth,
                                 int screenHeight, ProximitySensorListener proximity, int finalImg)
    {
        super(context);
        init(context);
        m_requiredIngredientsImages = ingredientsImages;
        m_mixImages         = mixImages;
        m_screenWidth       = screenWidth;
        m_screenHeight      = screenHeight;
        m_currIngredientIdx = randomIngredientIdx();
        m_ingredientX       = screenWidth / 2;
        m_ingredientY       = screenHeight / 2;
        m_finalImg          = finalImg;
        m_proximitySensorListener = proximity;
    }



    public ChooseIngredientsView(Context context)
    {
        super(context);
        init(context);
    }



    public ChooseIngredientsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }



    public ChooseIngredientsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    public void init(Context context)
    {
        getHolder().addCallback(this);
        setFocusable(true);
        m_context = context;
        m_ingredientPaint = new Paint();
        m_collectedBackgroundPaint = new Paint();
        m_collectedBackgroundPaint.setColor(COLLECTED_BACKGROUND_COLOR);
        m_collectedBackgroundPaint.setAlpha(COLLECTED_BACKGROUND_OPACITY);
        m_vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        m_collectedIngredients = new ArrayList<>();
        m_kitchen = BitmapFactory.decodeResource(context.getResources(), R.drawable.kitchen);
        m_kitchenRed = BitmapFactory.decodeResource(context.getResources(), R.drawable.kitchen_red);
    }



    // Game logic /////////////////////////////////////////////////////////////////////////////////



    public void render(Canvas canvas)
    {
        drawBackground(canvas);
        drawIngredient(canvas, m_currIngredientIdx);
        drawCollected(canvas);
    }




    public void tick()
    {
        tickIngredient();
        if (m_proximitySensorListener.isClose())
            catchIngredient();
        if (m_requiredIngredientsImages.isEmpty())
        {
            endChoosing();
        }
    }



    private void endChoosing()
    {
        m_gameThread.setRunning(false);
        Intent intent = new Intent(m_context, MixActivity.class);
        intent.putIntegerArrayListExtra(MixActivity.PHOTOS_KEY, m_mixImages);
        intent.putExtra(MixActivity.FINAL_PHOTO_KEY, m_finalImg);
        m_context.startActivity(intent);
        ((Activity) m_context).finish();
    }



    private int randomIngredientIdx()
    {
        boolean success = false;
        int randIdx     = 0;
        while (!success)
        {
            randIdx = (int) (Math.random() * ALL_INGREDIENTS.length);
            if (!m_collectedIngredients.contains(ALL_INGREDIENTS[randIdx]))
                success = true;
        }
        return randIdx;
    }



    private void tickIngredient()
    {
        if (System.currentTimeMillis() - m_lastIngredientChange > TIME_BETWEEN_INGREDIENT_CHANGES)
        {
            chooseNewIngredient();
            m_ingredientX = (int) (Math.random() * (m_screenWidth - INGREDIENT_SIZE));
            m_ingredientY = (int) (Math.random() * (m_screenHeight - INGREDIENT_SIZE -
                    COLLECTED_INGREDIENT_SIZE));
            m_lastIngredientChange = System.currentTimeMillis();
        }
    }



    private void chooseNewIngredient()
    {
        int randIngrAll     = randomIngredientIdx();
        int randIngrReqir   = (int) (Math.random() * m_requiredIngredientsImages.size());
        m_currIngredientIdx = Math.random() < NEW_INGREDIENT_ALL_PERCENTAGE ?
                randIngrAll : indexOfIngredient(m_requiredIngredientsImages.get(randIngrReqir));
        m_ingredientX       = -INGREDIENT_SIZE;
    }



    private int indexOfIngredient(int ingredient)
    {
        for (int i = 0; i < ALL_INGREDIENTS.length; ++i)
        {
            if (ALL_INGREDIENTS[i] == ingredient)
                return i;
        }

        return -1;
    }



    private void catchIngredient()
    {
        if (m_requiredIngredientsImages.contains(ALL_INGREDIENTS[m_currIngredientIdx]))
            correctCatch();
        else
            badCatch();
    }



    private void correctCatch()
    {
        m_requiredIngredientsImages.remove(Integer.valueOf(ALL_INGREDIENTS[m_currIngredientIdx]));
        m_collectedIngredients.add(ALL_INGREDIENTS[m_currIngredientIdx]);
    }



    private void badCatch()
    {
        if (m_collectedIngredients.contains(ALL_INGREDIENTS[m_currIngredientIdx]))  // when player collects again, before current disappears
            return;

        m_badChoice       = true;
        m_displayBadStart = System.currentTimeMillis();
        m_vibrator.vibrate(VIBRATION_TIME_WHEN_BAD_INGREDIENT);

        // remove from collected
        if (!m_collectedIngredients.isEmpty())
        {
            int last = m_collectedIngredients.remove(m_collectedIngredients.size() - 1);
            m_requiredIngredientsImages.add(last);
        }
    }



    // Drawing ////////////////////////////////////////////////////////////////////////////////////



    private void drawBackground(Canvas canvas)
    {
        if (m_badChoice)
        {
            canvas.drawBitmap(m_kitchenRed,0, 0, new Paint());
            if (System.currentTimeMillis() - m_displayBadStart > BAD_CHOICE_DISPLAY_TIME)
            {
                m_badChoice = false;
            }
        }
        else
            canvas.drawBitmap(m_kitchen,0, 0, new Paint());
    }



    private void drawIngredient(Canvas canvas, int ingredientIdx)
    {
        Bitmap img = BitmapFactory.decodeResource(m_context.getResources(),
                ALL_INGREDIENTS[ingredientIdx]);
        img = Bitmap.createScaledBitmap(img, INGREDIENT_SIZE, INGREDIENT_SIZE, false);
        canvas.drawBitmap(img, m_ingredientX, m_ingredientY, m_ingredientPaint);
    }



    private void drawCollected(Canvas canvas)
    {
        drawCollectedBackground(canvas);
        int x = COLLECTED_BACKGROUND_MARGIN;
        Bitmap ingrBitmap;
        int collected;
        for (int i = 0; i < m_collectedIngredients.size(); ++i)
        {
            collected = m_collectedIngredients.get(i);
            ingrBitmap = BitmapFactory.decodeResource(m_context.getResources(), collected);
            ingrBitmap = Bitmap.createScaledBitmap(ingrBitmap, COLLECTED_INGREDIENT_SIZE,
                    COLLECTED_INGREDIENT_SIZE, false);

            canvas.drawBitmap(ingrBitmap, x, m_screenHeight - COLLECTED_INGREDIENT_SIZE -
                            COLLECTED_BACKGROUND_MARGIN,
                    m_ingredientPaint);

            x += COLLECTED_INGREDIENT_SIZE;
        }
    }



    private void drawCollectedBackground(Canvas canvas)
    {
        if (m_collectedIngredients.isEmpty())
            return;

        RectF bg = new RectF();
        bg.left   = COLLECTED_BACKGROUND_MARGIN;
        bg.top    = m_screenHeight - COLLECTED_INGREDIENT_SIZE - COLLECTED_BACKGROUND_MARGIN;
        bg.right  = m_collectedIngredients.size() * COLLECTED_INGREDIENT_SIZE + COLLECTED_BACKGROUND_MARGIN;
        bg.bottom = m_screenHeight - COLLECTED_BACKGROUND_MARGIN;
        canvas.drawRoundRect(
                bg,
                COLLECTED_BACKGROUND_CORNER,
                COLLECTED_BACKGROUND_CORNER,
                m_collectedBackgroundPaint);
    }



    // Controls //////////////////////////////////////////////////////////////////////////////////



    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        performClick();
        return true;
    }


    @Override
    public boolean performClick() {
        catchIngredient();
        return super.performClick();
    }



    // Surface ////////////////////////////////////////////////////////////////////////////////////



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }




    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        m_gameThread = new GameThread(this);
        m_gameThread.setRunning(true);
        m_gameThread.start();
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (m_gameThread == null)
            return;

        m_gameThread.setRunning(false);
        try
        {
            m_gameThread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}

