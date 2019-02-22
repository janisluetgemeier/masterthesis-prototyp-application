package de.luetgemeier.masterthesis.prototype.application.services;

import de.luetgemeier.masterthesis.prototype.application.entities.Datei;
import de.luetgemeier.masterthesis.prototype.application.repositories.DateiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class CleanUpService {

    @Autowired
    private DateiRepository dateiRepository;

    @Scheduled(cron = "0 2 * * *")
    public void cleanUpOldFiles() {

        Iterable<Datei> dateien = dateiRepository.findAll();

        //Datum vor 90 Tagen instanziieren:
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -90);

        Date vorNeunzigTagen = calendar.getTime();
        dateien.forEach(datei -> {
            if (null != datei.getCreateddate()) {
                if (vorNeunzigTagen.after(datei.getCreateddate())) {
                    dateiRepository.delete(datei);
                }
            }

        });

    }
}
