package com.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

public class DBGenerator {

    public static void main(String[] args) throws Exception {
        final Schema schema = new Schema(7, "ru.nsu.fit.popov.medicineclock.model.db");

        addSysDate(schema);
        Entity medicine = addMedicine(schema);
        addLogging(schema, medicine);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static Entity addSysDate(Schema schema) {
        Entity system = schema.addEntity("SysDate");
        system.addIdProperty().primaryKey().autoincrement();
        system.addBooleanProperty("active");

        return system;
    }

    private static Entity addMedicine(Schema schema) {
        Entity medicine = schema.addEntity("Medicine");
        medicine.addIdProperty().primaryKey().autoincrement();
        medicine.addStringProperty("name").unique().notNull();
        medicine.addBooleanProperty("active").notNull();
        medicine.addIntProperty("startTime").notNull();
        medicine.addIntProperty("delay").notNull();
        medicine.addIntProperty("count").notNull();

        return medicine;
    }

    private static Entity addLogging(Schema schema, Entity medicine) {
        Entity logging = schema.addEntity("Logging");
        logging.addIdProperty().primaryKey().autoincrement();
        logging.addDateProperty("date");
        logging.addStringProperty("type");

        Property medicineID = logging.addLongProperty("medicineID").getProperty();
        logging.addToOne(medicine, medicineID);

        return logging;
    }
}
