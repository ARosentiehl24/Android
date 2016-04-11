package com.arrg.android.app.android.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arrg.android.app.android.R;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculatorActivity extends AppCompatActivity {

    private Boolean clear = false;
    private Evaluator evaluator;
    private String expression = "";

    @Bind(R.id.bLeftParenthesis)
    Button bLeftParenthesis;

    @Bind(R.id.bRightParenthesis)
    Button bRightParenthesis;

    @Bind(R.id.bSeven)
    Button bSeven;

    @Bind(R.id.bEight)
    Button bEight;

    @Bind(R.id.bNine)
    Button bNine;

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
                answer.setText("");
                equation.setText("");
                expression = "";
                break;
            case R.id.bLeftParenthesis:
                concatText(bLeftParenthesis.getText().toString());
                break;
            case R.id.bRightParenthesis:
                concatText(bRightParenthesis.getText().toString());
                break;
            case R.id.bDivision:
                concatText("/");
                break;
            case R.id.bSeven:
                concatText(bSeven.getText().toString());
                break;
            case R.id.bEight:
                concatText(bEight.getText().toString());
                break;
            case R.id.bNine:
                concatText(bNine.getText().toString());
                break;
            case R.id.bMultiplication:
                concatText("*");
                break;
            case R.id.bFour:
                concatText(bFour.getText().toString());
                break;
            case R.id.bFive:
                concatText(bFive.getText().toString());
                break;
            case R.id.bSix:
                concatText(bSix.getText().toString());
                break;
            case R.id.bSubtract:
                concatText(bSubtract.getText().toString());
                break;
            case R.id.bOne:
                concatText(bOne.getText().toString());
                break;
            case R.id.bTwo:
                concatText(bTwo.getText().toString());
                break;
            case R.id.bThree:
                concatText(bThree.getText().toString());
                break;
            case R.id.bAdd:
                concatText(bAdd.getText().toString());
                break;
            case R.id.bDot:
                concatText(bDot.getText().toString());
                break;
            case R.id.bZero:
                concatText(bZero.getText().toString());
                break;
            case R.id.bDelete:
                if (expression.length() != 0) {
                    equation.setText(expression.substring(0, expression.length() - 1));
                    expression = equation.getText().toString();
                } else if (expression.length() == 0) {
                    answer.setText("");
                }
                break;
            case R.id.bEqual:
                expression = answer.getText().toString();
                equation.setText(answer.getText());
                answer.setText("");

                clear = true;

                break;
        }

        if (expression.length() == 0) {
            answer.setText("");
        } else {
            try {
                answer.setText(evaluator.evaluate(expression));
            } catch (EvaluationException e) {
                Log.e("Expression", getString(R.string.wrong_expression_message));
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        evaluator = new Evaluator();
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

        if (clear && !isAMathematicalSign(lastIndexOf(text)) && !isAMathematicalSign(lastIndexOf(expression))) {
            expression = "";
            equation.setText("");

            clear = false;
        } else {
            clear = false;
        }

        if (expression.length() != 0) {
            if (isAMathematicalSign(lastIndexOf(text)) && isAMathematicalSign(lastIndexOf(expression))) {
                expression = expression.substring(0, expression.length() - 1);
            }

            if (text.equals("(") && (!isAMathematicalSign(lastIndexOf(expression)))) {
                expression += "*";
            }
        }

        expression += text;
        equation.setText(expression);
    }

    public char lastIndexOf(String expression) {
        return expression.charAt(expression.length() - 1);
    }

    public boolean isAMathematicalSign(char value) {
        return value == '+' || value == '-' || value == '*' || value == '/';
    }

    public boolean isAMathematicalSignWithOutSubtract(char value) {
        return value == '+' || value == '*' || value == '/';
    }
}
