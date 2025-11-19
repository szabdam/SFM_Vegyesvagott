package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class CsomagService {
    private static final List<Csomag> csomagok = new ArrayList<>();

    public void hozzaad(Csomag csomag) {
        csomagok.add(csomag);
    }

    public List<Csomag> getOsszes() {
        return new ArrayList<>(csomagok);
    }
}
