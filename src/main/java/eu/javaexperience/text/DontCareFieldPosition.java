package eu.javaexperience.text;

import java.text.FieldPosition;

/**
 * identically the same as java.text.DontCareFieldPosition
 * the only but the important difference is that this is a public class.
 * 
 * DontCareFieldPosition defines no-op FieldDelegate. Its
 * singleton is used for the format methods that don't take a
 * FieldPosition.
 */
public class DontCareFieldPosition extends FieldPosition
{
    // The singleton of DontCareFieldPosition.
    public static final FieldPosition INSTANCE = new DontCareFieldPosition();


    private DontCareFieldPosition() {
        super(0);
    }
}
