package edu.illinois.cs.cs125.mp5;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.illinois.cs.cs125.mp5.lib.ChemicalElement;
import edu.illinois.cs.cs125.mp5.lib.CyclicOrganicMoleculeBuilder;
import edu.illinois.cs.cs125.mp5.lib.LinearOrganicMoleculeBuilder;
import edu.illinois.cs.cs125.mp5.lib.OrganicMoleculeBuilder;

/**
 * Activity class that handles the main screen of the app.
 * <p>
 * This class is responsible for initializing the app's home page
 * and rendering the molecule data onto the screen.
 */
public class MoleculeActivity extends Activity {
    /**
     * Tag for logging.
     */
    private static final String TAG = "MP5:MainActivity";

    /**
     * The Android WebView object that displays the molecule drawn on top of a web canvas.
     */
    private WebView webView;

    /**
     * A list of MoleculeData objects where each element is a molecule that can be rendered.
     */
    private ArrayList<MoleculeData> loadedMolecules;

    /**
     * The index of the molecule in the loadedMolecules list that is currently being displayed.
     */
    private int currentMoleculeIndex = 0;

    @SuppressLint("SetJavascriptEnabled")
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        // Set our default layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_molecule);

        // Load all user-defined molecules
        loadedMolecules = loadMolecules();

        // Set up our webview to display the current molecule
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(final WebView unusedWebView, final String unusedString) {
                loadMolecule(currentMoleculeIndex);
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
    }

    /**
     * Return an ArrayList of molecules that can be viewed and analyzed.
     * <p>
     * You should feel free to add your own molecules here to aid in debugging your library.
     *
     * @return an array of molecules
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private ArrayList<MoleculeData> loadMolecules() {
        ArrayList<MoleculeData> newMolecules = new ArrayList<>();

        newMolecules.add(new MoleculeData((LinearOrganicMoleculeBuilder) new LinearOrganicMoleculeBuilder(4)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl())));
        newMolecules.add(new MoleculeData((LinearOrganicMoleculeBuilder) new LinearOrganicMoleculeBuilder(2)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))));
        newMolecules.add(new MoleculeData((LinearOrganicMoleculeBuilder) new LinearOrganicMoleculeBuilder(5)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(3))
                .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createCarbonyl())));
        newMolecules.add(new MoleculeData((CyclicOrganicMoleculeBuilder) new CyclicOrganicMoleculeBuilder(4)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())));
        newMolecules.add(new MoleculeData((CyclicOrganicMoleculeBuilder) new CyclicOrganicMoleculeBuilder(4)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(1))));
        newMolecules.add(new MoleculeData((CyclicOrganicMoleculeBuilder) new CyclicOrganicMoleculeBuilder(5)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createCarbonyl())));
        newMolecules.add(new MoleculeData((CyclicOrganicMoleculeBuilder) new CyclicOrganicMoleculeBuilder(3)
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol())
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))));

        // Add your own new molecules here!

        return newMolecules;
    }

    /**
     * Load and display the next molecule.
     *
     * @param unused used Android view object
     */
    public void onNextButtonClick(final View unused) {
        currentMoleculeIndex = (currentMoleculeIndex + 1) % loadedMolecules.size();
        loadMolecule(currentMoleculeIndex);
    }

    /**
     * Loads a new molecule onto the WebView.
     *
     * @param moleculeIndex the index of the molecule to load in our molecule ArrayList
     */
    private void loadMolecule(final int moleculeIndex) {
        MoleculeData moleculeToLoad;
        try {
            moleculeToLoad = loadedMolecules.get(moleculeIndex);
        } catch (IndexOutOfBoundsException unused) {
            Log.w(TAG, "Tried to load molecule at invalid index: " + moleculeIndex);
            return;
        }

        // Serialize to JSON for transfer to Javascript
        String moleculeAsJSON = new Gson().toJson(moleculeToLoad);

        // evaluateJavascript() is only available on newer devices, but greatly improves reliability.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:loadMolecule('" + moleculeAsJSON + "');", null);
        } else {
            webView.loadUrl("javascript:loadMolecule('" + moleculeAsJSON + "');");
        }

        // Update the Ui with various pieces of information about the molecule
        final TextView formulaTextView = findViewById(R.id.text_formula);
        formulaTextView.setText(moleculeToLoad.getFormula());

        final TextView nameTextView = findViewById(R.id.text_name);
        nameTextView.setText(moleculeToLoad.getName());

        final TextView massTextView = findViewById(R.id.text_mass);
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        massTextView.setText(String.format("%s amu", decimalFormat.format(moleculeToLoad.getMass())));
    }
}
