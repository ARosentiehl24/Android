package com.arrg.android.app.android.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arrg.android.app.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculatorActivity extends AppCompatActivity {

    @Bind(R.id.bClearAll)
    Button bClearAll;

    @Bind(R.id.bLeftParenthesis)
    Button bLeftParenthesis;

    @Bind(R.id.bRightParenthesis)
    Button bRightParenthesis;

    @Bind(R.id.bDivision)
    Button bDivision;

    @Bind(R.id.bSeven)
    Button bSeven;

    @Bind(R.id.bEight)
    Button bEight;

    @Bind(R.id.bNine)
    Button bNine;

    @Bind(R.id.bMultiplication)
    Button bMultiplication;

    @Bind(R.id.bFour)
    Button bFour;

    @Bind(R.id.bFive)
    Button bFive;

    @Bind(R.id.bSix)
    Button bSix;

    @Bind(R.id.bSubtract)
    Button bSubtract;

    @Bind(R.id.bOne)
    Button bOne;

    @Bind(R.id.bTwo)
    Button bTwo;

    @Bind(R.id.bThree)
    Button bThree;

    @Bind(R.id.bAdd)
    Button bAdd;

    @Bind(R.id.bDot)
    Button bDot;

    @Bind(R.id.bZero)
    Button bZero;

    @Bind(R.id.bDelete)
    ImageButton bDelete;

    @Bind(R.id.bEqual)
    Button bEqual;

    @Bind(R.id.equation)
    TextView equation;

    @Bind(R.id.answer)
    TextView answer;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @OnClick({R.id.bClearAll, R.id.bLeftParenthesis, R.id.bRightParenthesis, R.id.bDivision, R.id.bSeven, R.id.bEight, R.id.bNine, R.id.bMultiplication,
            R.id.bFour, R.id.bFive, R.id.bSix, R.id.bSubtract, R.id.bOne, R.id.bTwo, R.id.bThree, R.id.bAdd, R.id.bDot, R.id.bZero, R.id.bDelete, R.id.bEqual})
    public void OnClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.bClearAll:
                break;
            case R.id.bLeftParenthesis:
                break;
            case R.id.bRightParenthesis:
                break;
            case R.id.bDivision:
                break;
            case R.id.bSeven:
                break;
            case R.id.bEight:
                break;
            case R.id.bNine:
                break;
            case R.id.bMultiplication:
                break;
            case R.id.bFour:
                break;
            case R.id.bFive:
                break;
            case R.id.bSix:
                break;
            case R.id.bSubtract:
                break;
            case R.id.bOne:
                break;
            case R.id.bTwo:
                break;
            case R.id.bThree:
                break;
            case R.id.bAdd:
                break;
            case R.id.bDot:
                break;
            case R.id.bZero:
                break;
            case R.id.bDelete:
                break;
            case R.id.bEqual:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void concatText(String text) {
        equation.setText(equation.getText().toString().concat(text));
    }
}
