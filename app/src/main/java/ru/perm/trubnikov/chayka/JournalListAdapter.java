package ru.perm.trubnikov.chayka;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JournalListAdapter extends ArrayAdapter<String> {

    protected final String[] contactids;
    protected final Context context;
    protected final String[] firstlines;
    protected final String[] secondlines;
    protected final Integer[] incomings;
    protected final Integer[] outgoings;
    protected final Integer[] misseds;

    public JournalListAdapter(Context context, String[] firstlines, String[] secondlines, String[] contactids, Integer[] incomings, Integer[] outgoings, Integer[] misseds) {
        super(context, R.layout.journal_list_item, secondlines); // !!!
        this.context = context;
        this.firstlines = firstlines;
        this.secondlines = secondlines;
        this.contactids = contactids;
        this.incomings = incomings;
        this.outgoings = outgoings;
        this.misseds = misseds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.journal_list_item, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);

        // Текст
        if (firstlines[position] == null) {
            firstlines[position] = context.getString(R.string.unknown_contact);
        }

        firstLine.setText(firstlines[position]);
        secondLine.setText(secondlines[position]);

        // Картинки
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        ImageView imageView2 = (ImageView) rowView.findViewById(R.id.icon2);
        ImageView imageView3 = (ImageView) rowView.findViewById(R.id.icon3);
        ImageView imageView4 = (ImageView) rowView.findViewById(R.id.icon4);

        try {
            if (contactids[position] != null) {
                int photo_id = Integer.parseInt(contactids[position]);
                if (photo_id > 0) {
                    Bitmap bitmap = queryContactImage(Integer.parseInt(contactids[position]));
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                } else {
                    imageView.setImageResource(R.drawable.ic_person_grey);
                }
            }

            int incoming = incomings[position];
            int outgoing = outgoings[position];
            int missed = misseds[position];

            if (incoming > 0) {
                imageView2.setImageResource(R.drawable.incoming);
                incoming = 0;
            } else if (outgoing > 0) {
                imageView2.setImageResource(R.drawable.outgoing);
                outgoing = 0;
            } else if (missed > 0) {
                imageView2.setImageResource(R.drawable.missed);
                missed = 0;
            }

            if (incoming > 0) {
                imageView3.setImageResource(R.drawable.incoming);
                incoming = 0;
            } else if (outgoing > 0) {
                imageView3.setImageResource(R.drawable.outgoing);
                outgoing = 0;
            } else if (missed > 0) {
                imageView3.setImageResource(R.drawable.missed);
                missed = 0;
            }

            if (incoming > 0) {
                imageView4.setImageResource(R.drawable.incoming);
            } else if (outgoing > 0) {
                imageView4.setImageResource(R.drawable.outgoing);
            } else if (missed > 0) {
                imageView4.setImageResource(R.drawable.missed);
            }

        } catch (Exception e) {
            //
        }

        return rowView;
    }


    private Bitmap queryContactImage(int imageDataRow) {
        Cursor c = this.context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                ContactsContract.CommonDataKinds.Photo.PHOTO
        }, ContactsContract.Data._ID + "=?", new String[]{
                Integer.toString(imageDataRow)
        }, null);
        byte[] imageBytes = null;
        if (c != null) {
            if (c.moveToFirst()) {
                imageBytes = c.getBlob(0);
            }
            c.close();
        }

        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }


}
