package com.example.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingappclient.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class TicketDisplayActivity extends AppCompatActivity {
    private String ticketId, entryTime, tenantName, vehicleNo, qrPayload;
    private Bitmap qrBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_display);
       if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        TextView ticketIdTextView = findViewById(R.id.textViewTicketID);
        TextView entryTimeTextView = findViewById(R.id.textViewEntryTime);
        TextView tenantTextView = findViewById(R.id.textViewTenant);
        TextView vehicleTextView = findViewById(R.id.textViewVehicle);
        ImageView qrCodeImageView = findViewById(R.id.imageViewQrCode);
        Button btnPrintEntryTicket = findViewById(R.id.btn_print_entry_ticket);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            this.ticketId = extras.getString("TICKET_ID", "N/A");
            this.entryTime = extras.getString("ENTRY_TIME", "N/A");
            this.tenantName = extras.getString("TENANT_NAME", "N/A");
            this.vehicleNo = extras.getString("VEHICLE_NO", "N/A");
            this.qrPayload = extras.getString("QR_PAYLOAD", "N/A");
            ticketIdTextView.setText(this.ticketId);
            entryTimeTextView.setText(this.entryTime);
            tenantTextView.setText(this.tenantName);
            vehicleTextView.setText(this.vehicleNo);
            try
            {
                this.qrBitmap = generateQrCode(this.qrPayload);
                qrCodeImageView.setImageBitmap(this.qrBitmap);
                applyPulseAnimation(qrCodeImageView);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            btnPrintEntryTicket.setOnClickListener(v -> printTicketAndRedirect());
            }
        else
        {
            Toast.makeText(this, "Ticket data missing!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void applyPulseAnimation(ImageView img) {
        ScaleAnimation pulse = new ScaleAnimation(0.95f, 1.05f, 0.95f, 1.05f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        pulse.setDuration(800);
        pulse.setRepeatCount(Animation.INFINITE);
        pulse.setRepeatMode(Animation.REVERSE);
        img.startAnimation(pulse);
    }
    private void printTicketAndRedirect()
    {
        Toast.makeText(this, "Sending to Bluetooth Printer...", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(() ->
        {
            Toast.makeText(this, "Entry Ticket Printed!", Toast.LENGTH_SHORT).show();
            redirectToDashboard();
        }
        , 1500);
    }

    private void redirectToDashboard()
    {
        Intent intent = new Intent(this, SecurityDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private Bitmap generateQrCode(String content) throws WriterException
    {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 400, 400);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}