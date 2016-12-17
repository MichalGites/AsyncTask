package pl.akademiakodu.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button button;

    // ilosc asyncTaskow w watku roboczym wzor CPUcores*2+1
    // średnio czasochłonne zadania do 1 minuty pobranie info z neta, bazy danych itp
    // pobieranie pliku to już będzie wątek
    // na jednej instancji asyncTasku mozemy uruchomic tylko jedno zadanie jeden raz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.buttonStart);

        new FakeTask().execute(/* tutaj argumenty jeśli klasa przyjmuje - my mamy void */);
    }

    // 1. (typ generyczny) Jaki parametr przyjmuje doInBackground
    // 2. cp przyjmuje on ProgressUpdate
    // 3. co zwraca metoda doInBackground i onPostExecute
    private class FakeTask extends AsyncTask<Void, Integer, Void>{

        // ta metoda wykonuje sie w innym watku - dzialanie w tle
        @Override
        protected Void doInBackground(Void... strings) {
            // wielokropek pozwala nam wpisywac wiele argumetow typu String - tworzy taka tablice argumentow
            //publishProgress("20%", "asd", "asaas");
            for (int i=0; i<6; i++){
                publishProgress(i);
                try {
                    // To jest nieprawdziwe zadanie celem prezentacji
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // pozostale wykonuja sie w watku glownym
        @Override
        protected void onPreExecute() {
            // ta metoda wykonuje sie przed wykonaniem doInBackground
            // dzieki temu this odnosimy sie do calej klasy MainActivity,
            // bo samo this tylko odnosi nas tutaj do klasy FakeTask (zagnieżdżona klasa)
            Toast.makeText(MainActivity.this, "Rozpoczynam pobieranie", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void voids) {
            // ta metoda wykonuje sie po wykonaniu doInBackground
            Toast.makeText(MainActivity.this, "Pobieranie zakończone", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // ta metoda wykonuje sie w trakcie wykonywania doInBackground
            // mozna dzieki niej edytowac progressBar np
            switch (values[0]){
                case 1: {
                    progressBar.setProgress(20);
                    break;
                }
                case 2: {
                    progressBar.setProgress(40);
                    break;
                }
                case 3: {
                    progressBar.setProgress(60);
                    break;
                }
                case 4: {
                    progressBar.setProgress(80);
                    break;
                }
                case 5: {
                    progressBar.setProgress(100);
                    break;
                }
            }
        }
    }

}
