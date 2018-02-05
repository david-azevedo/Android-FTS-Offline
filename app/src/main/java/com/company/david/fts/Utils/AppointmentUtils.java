package com.company.david.fts.Utils;

import java.util.Random;

public class AppointmentUtils {

    private static final Random mRANDOM = new Random();

    private static final String[] FAKE_HOSPITALS = new String[] {
            "Hospital S. João",
            "Pedro Híspano",
            "Nossa Senhora do Amparo",
            "IPO",
            "Dentista Rui Lopes",
            "Alberto Caeiro Ortopedista"
    };

    private static final String[] FAKE_DOCTORS = new String[] {
            "Pedro Cabral",
            "Rui Lopes",
            "Alberto Caeiro",
            "Fernando Gente",
            "João Monteiro",
            "David Azevedo",
            "Carla Lopes",
            "Felisberto Choramuito",
            "Aquele médico meio vesgo"
    };

    public static final String[] getFakeData() {
        String hospital = FAKE_HOSPITALS[mRANDOM.nextInt(FAKE_HOSPITALS.length)];
        String doctor = FAKE_DOCTORS[mRANDOM.nextInt(FAKE_DOCTORS.length)];
        String transcript = LoremIpsum.randomCorpus(mRANDOM.nextInt(5) + 5);
        return new String[] {hospital, doctor, transcript};
    }
}
