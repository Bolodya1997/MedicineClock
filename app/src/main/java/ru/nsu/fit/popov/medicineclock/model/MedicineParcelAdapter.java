package ru.nsu.fit.popov.medicineclock.model;

import android.os.Parcel;
import android.os.Parcelable;

import ru.nsu.fit.popov.medicineclock.model.db.Medicine;

public class MedicineParcelAdapter implements Parcelable {

    private static final long NULL_VALUE = -1;

    public static final Creator<MedicineParcelAdapter> CREATOR = new Creator<MedicineParcelAdapter>() {
        @Override
        public MedicineParcelAdapter createFromParcel(Parcel in) {
            return new MedicineParcelAdapter(in);
        }

        @Override
        public MedicineParcelAdapter[] newArray(int size) {
            return new MedicineParcelAdapter[size];
        }
    };

    private Medicine medicine;

    public MedicineParcelAdapter(Medicine medicine) {
        this.medicine = medicine;
    }

    protected MedicineParcelAdapter(Parcel in) {
        Long id = in.readLong();
        if (id == NULL_VALUE) {
            medicine = new Medicine(null);
        } else {
            medicine = new Medicine(id, in.readString(), in.readByte() != 0, in.readInt(),
                    in.readInt(), in.readInt());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Long id = medicine.getId();
        if (id == null)
            id = NULL_VALUE;

        parcel.writeLong(id);
        if (id != NULL_VALUE) {
            parcel.writeString(medicine.getName());
            parcel.writeByte((byte) (medicine.getActive() ? 1 : 0));
            parcel.writeInt(medicine.getStartTime());
            parcel.writeInt(medicine.getDelay());
            parcel.writeInt(medicine.getCount());
        }
    }

    public Medicine getMedicine() {
        return medicine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MedicineParcelAdapter that = (MedicineParcelAdapter) o;

        return medicine != null ? medicine.getId().equals(that.medicine.getId())
                                : that.medicine == null;

    }

    @Override
    public int hashCode() {
        return medicine != null ? medicine.getId().hashCode()
                                : 0;
    }
}
