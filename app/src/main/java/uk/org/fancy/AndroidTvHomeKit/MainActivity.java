package uk.org.fancy.AndroidTvHomeKit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.UUID;
import uk.org.fancy.AndroidTvHomeKit.Philips.Television;
import uk.org.fancy.AndroidTvHomeKit.Philips.xtv.XTvHttp;

public class MainActivity extends Activity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ImageView qrImageView;
    private TextView pinTextView;
    private TextView statusTitleView;
    private TextView statusDetailsView;
    private TextView xtvStatusView;
    private String lastRenderedPin = null;

    private final Runnable pollUiRunnable = new Runnable() {
        @Override
        public void run() {
            updateStatusUi();
            handler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startHomeKitService();
        setContentView(buildDashboardView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(pollUiRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(pollUiRunnable);
    }

    private void startHomeKitService() {
        Intent serviceIntent = new Intent(this, HomeKitService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            getResources().getDisplayMetrics()
        );
    }

    private View buildDashboardView() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setBackgroundColor(Color.parseColor("#0D1117"));
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(48), dp(36), dp(48), dp(36));

        // Left Card: QR Code & Setup PIN
        LinearLayout leftCard = new LinearLayout(this);
        leftCard.setOrientation(LinearLayout.VERTICAL);
        leftCard.setGravity(Gravity.CENTER);
        leftCard.setPadding(dp(28), dp(28), dp(28), dp(28));
        GradientDrawable leftBg = new GradientDrawable();
        leftBg.setColor(Color.parseColor("#161B22"));
        leftBg.setCornerRadius(dp(20));
        leftBg.setStroke(dp(1), Color.parseColor("#30363D"));
        leftCard.setBackground(leftBg);

        TextView qrHeader = new TextView(this);
        qrHeader.setText("Apple Ev (HomeKit) Kurulum Kodu");
        qrHeader.setTextColor(Color.WHITE);
        qrHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        qrHeader.setTypeface(Typeface.DEFAULT_BOLD);
        qrHeader.setGravity(Gravity.CENTER);
        leftCard.addView(qrHeader);

        TextView qrSub = new TextView(this);
        qrSub.setText("iPhone veya iPad'de Ev (Home) uygulamasını açıp\n'Aksesuar Ekle' ile bu QR kodu okutun");
        qrSub.setTextColor(Color.parseColor("#8B949E"));
        qrSub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        qrSub.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        subLp.topMargin = dp(6);
        subLp.bottomMargin = dp(16);
        leftCard.addView(qrSub, subLp);

        qrImageView = new ImageView(this);
        LinearLayout.LayoutParams qrLp = new LinearLayout.LayoutParams(dp(220), dp(220));
        leftCard.addView(qrImageView, qrLp);

        pinTextView = new TextView(this);
        pinTextView.setText("031-45-154");
        pinTextView.setTextColor(Color.parseColor("#58A6FF"));
        pinTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        pinTextView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        pinTextView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams pinLp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        pinLp.topMargin = dp(14);
        leftCard.addView(pinTextView, pinLp);

        renderQrForPin("031-45-154");

        LinearLayout.LayoutParams leftCardLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        leftCardLp.rightMargin = dp(28);
        root.addView(leftCard, leftCardLp);

        // Right Card: Status & Controls
        LinearLayout rightCard = new LinearLayout(this);
        rightCard.setOrientation(LinearLayout.VERTICAL);
        rightCard.setGravity(Gravity.CENTER_VERTICAL);
        rightCard.setPadding(dp(32), dp(28), dp(32), dp(28));
        GradientDrawable rightBg = new GradientDrawable();
        rightBg.setColor(Color.parseColor("#161B22"));
        rightBg.setCornerRadius(dp(20));
        rightBg.setStroke(dp(1), Color.parseColor("#30363D"));
        rightCard.setBackground(rightBg);

        statusTitleView = new TextView(this);
        statusTitleView.setText("Philips Android TV HomeKit");
        statusTitleView.setTextColor(Color.WHITE);
        statusTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        statusTitleView.setTypeface(Typeface.DEFAULT_BOLD);
        rightCard.addView(statusTitleView);

        statusDetailsView = new TextView(this);
        statusDetailsView.setText("HomeKit servisi kontrol ediliyor...");
        statusDetailsView.setTextColor(Color.parseColor("#C9D1D9"));
        statusDetailsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams detailsLp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        detailsLp.topMargin = dp(12);
        rightCard.addView(statusDetailsView, detailsLp);

        xtvStatusView = new TextView(this);
        xtvStatusView.setText("Philips JointSPACE API: Kontrol ediliyor...");
        xtvStatusView.setTextColor(Color.parseColor("#7EE787"));
        xtvStatusView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams xtvLp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        xtvLp.topMargin = dp(8);
        xtvLp.bottomMargin = dp(20);
        rightCard.addView(xtvStatusView, xtvLp);

        Button pairXtvButton = createActionButton("Philips TV API Eşleştir (Otomatik / PIN)", "#238636");
        pairXtvButton.setOnClickListener(v -> startPhilipsApiPairing());
        rightCard.addView(pairXtvButton, createButtonLayoutParams());

        Button resetHomeKitButton = createActionButton("HomeKit Eşleşmesini Sıfırla (Yeniden Ekle)", "#1F6FEB");
        resetHomeKitButton.setOnClickListener(v -> {
            if (HomeKitService.instance != null) {
                HomeKitService.instance.resetHomeKitPairings();
                Toast.makeText(this, "HomeKit eşleşmesi sıfırlandı. Ev uygulamasından tekrar ekleyebilirsiniz.", Toast.LENGTH_LONG).show();
                updateStatusUi();
            }
        });
        rightCard.addView(resetHomeKitButton, createButtonLayoutParams());

        Button testAmbilightButton = createActionButton("Servisi Yeniden Başlat / Yenile", "#30363D");
        testAmbilightButton.setOnClickListener(v -> {
            startHomeKitService();
            updateStatusUi();
            Toast.makeText(this, "HomeKit servis durumu güncellendi.", Toast.LENGTH_SHORT).show();
        });
        rightCard.addView(testAmbilightButton, createButtonLayoutParams());

        LinearLayout.LayoutParams rightCardLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.35f);
        root.addView(rightCard, rightCardLp);

        pairXtvButton.requestFocus();
        return root;
    }

    private LinearLayout.LayoutParams createButtonLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.topMargin = dp(10);
        return lp;
    }

    private Button createActionButton(String text, String hexColor) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setAllCaps(false);
        btn.setTextColor(Color.WHITE);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        btn.setPadding(dp(18), dp(12), dp(18), dp(12));
        btn.setFocusable(true);
        btn.setFocusableInTouchMode(true);

        GradientDrawable normalBg = new GradientDrawable();
        normalBg.setColor(Color.parseColor(hexColor));
        normalBg.setCornerRadius(dp(10));

        GradientDrawable focusedBg = new GradientDrawable();
        focusedBg.setColor(Color.parseColor(hexColor));
        focusedBg.setCornerRadius(dp(10));
        focusedBg.setStroke(dp(3), Color.WHITE);

        btn.setBackground(normalBg);
        btn.setOnFocusChangeListener((v, hasFocus) -> v.setBackground(hasFocus ? focusedBg : normalBg));
        return btn;
    }

    private void renderQrForPin(String pin) {
        if (pin == null || pin.equals(lastRenderedPin)) return;
        lastRenderedPin = pin;
        pinTextView.setText(pin);
        try {
            String uri = HomeKitSetupPayload.generateSetupUri(pin, 31, HomeKitSetupPayload.DEFAULT_SETUP_ID);
            Bitmap bmp = Qr.encode(uri).toBitmap(dp(220));
            qrImageView.setImageBitmap(bmp);
        } catch (Exception e) {
            // Ignore QR error
        }
    }

    private void updateStatusUi() {
        HomeKitService svc = HomeKitService.instance;
        if (svc == null) {
            statusDetailsView.setText("Durum: HomeKit servisi başlatılıyor...");
            return;
        }

        AuthInfo auth = svc.getAuthInfo();
        String pin = auth != null ? auth.getPin() : "031-45-154";
        String mac = auth != null ? auth.getMac() : "-";
        boolean paired = auth != null && auth.isPaired();
        int pairedUsers = auth != null ? auth.getPairedUserCount() : 0;

        renderQrForPin(pin);

        String tvName = svc.implementation != null ? svc.implementation.getName() : "Philips TV";
        String tvModel = svc.implementation != null ? svc.implementation.getModel() : "-";
        String tvSerial = svc.implementation != null ? svc.implementation.getSerialNumber() : "-";

        statusTitleView.setText(tvName + " (" + tvModel + ")");

        StringBuilder sb = new StringBuilder();
        sb.append("• Sunucu Durumu: ").append(svc.isStarted() ? "Aktif (Port " + HomeKitService.PORT + ")" : "Başlatılıyor...").append("\n");
        sb.append("• HomeKit Eşleşme: ").append(paired ? "Eşleşti (" + pairedUsers + " iOS Cihazı)" : "Eşleşmeye Hazır (Ev Uygulamasından Ekleyin)").append("\n");
        sb.append("• Aksesuar Kimliği (MAC): ").append(mac).append("  |  Seri No: ").append(tvSerial).append("\n");
        sb.append("• Özellikler: Güç (Aç/Kapat), Ses Seviyesi, HDMI/Giriş Seçimi, Kumanda, Ambilight");
        statusDetailsView.setText(sb.toString());

        if (svc.implementation instanceof Television) {
            Television tv = (Television) svc.implementation;
            boolean hasCreds = tv.xtvhttp.hasCredentials();
            xtvStatusView.setText(hasCreds
                ? "Philips JointSPACE v6 API: Eşleşti (Kullanıcı: " + tv.xtvhttp.getUsername() + ")"
                : "Philips JointSPACE v6 API: Eşleşmedi (Aşağıdaki butonla eşleştirin)");
            xtvStatusView.setTextColor(Color.parseColor(hasCreds ? "#7EE787" : "#FFA657"));
        }
    }

    private void startPhilipsApiPairing() {
        HomeKitService svc = HomeKitService.instance;
        if (svc == null || !(svc.implementation instanceof Television)) {
            Toast.makeText(this, "Servis henüz hazır değil.", Toast.LENGTH_SHORT).show();
            return;
        }

        Television tv = (Television) svc.implementation;
        String newDeviceId = "hk_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        Toast.makeText(this, "Philips TV eşleşme isteği gönderiliyor...", Toast.LENGTH_SHORT).show();

        tv.xtvhttp.pairRequest(newDeviceId, "AndroidTvHomeKit").whenComplete((session, err) -> {
            if (err != null || session == null) {
                runOnUiThread(() -> Toast.makeText(this, "Eşleşme isteği hatası: " + err, Toast.LENGTH_LONG).show());
                return;
            }

            // Wait 1.2s for Toast PIN to appear in logcat, try auto-reading first
            handler.postDelayed(() -> {
                String autoPin = XTvHttp.readPinFromLogcat();
                if (autoPin != null && autoPin.length() == 4) {
                    completePhilipsPairing(tv, session, autoPin, true);
                } else {
                    showManualPinDialog(tv, session);
                }
            }, 1200);
        });
    }

    private void completePhilipsPairing(Television tv, XTvHttp.PairSession session, String pin, boolean wasAuto) {
        tv.xtvhttp.pairGrant(session, pin).whenComplete((ok, err) -> {
            runOnUiThread(() -> {
                if (Boolean.TRUE.equals(ok)) {
                    tv.saveCredentials(session.deviceId, session.authKey);
                    updateStatusUi();
                    Toast.makeText(
                        this,
                        (wasAuto ? "Otomatik PIN (" + pin + ") ile " : "") + "Philips TV API eşleşmesi tamamlandı!",
                        Toast.LENGTH_LONG
                    ).show();
                } else if (wasAuto) {
                    showManualPinDialog(tv, session);
                } else {
                    Toast.makeText(this, "PIN doğrulaması başarısız oldu.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void showManualPinDialog(Television tv, XTvHttp.PairSession session) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Ekranda görünen 4 haneli PIN");

        new AlertDialog.Builder(this)
            .setTitle("Philips TV PIN Kodu")
            .setMessage("TV ekranının altında beliren 4 haneli PIN kodunu girin:")
            .setView(input)
            .setPositiveButton("Onayla", (d, w) -> {
                String pin = input.getText().toString().trim();
                if (!pin.isEmpty()) {
                    completePhilipsPairing(tv, session, pin, false);
                }
            })
            .setNegativeButton("İptal", null)
            .show();
    }
}
