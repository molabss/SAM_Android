package br.com.constran.mobile.view.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

//import com.dm.zbar.android.scanner.ZBarConstants;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import br.com.constran.mobile.R;
import br.com.constran.mobile.enums.TipoModulo;
import br.com.constran.mobile.persistence.vo.Intervalo;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EventoVO;
import br.com.constran.mobile.persistence.vo.menu.ConfiguracoesVO;
import br.com.constran.mobile.qrcode.ZBarConstants;

public class Util extends Activity {

    private static final String ZERO = "0";
    private static final String DOT = ".";
    private static final String COMMA = ":";

    public static final Gson GSON_API = new Gson();

    private static final SimpleDateFormat dataBaseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dataBaseDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateHourMinFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final SimpleDateFormat dateHourMinWithoutSecondFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat foldersFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("ddMMyyyy");
    private static final SimpleDateFormat fileNamePartHourFormat = new SimpleDateFormat("HHmmss");
    private static final SimpleDateFormat secondsFormat = new SimpleDateFormat("ss");
    private static final SimpleDateFormat miliSecondsFormat = new SimpleDateFormat("SSS");

    private static final String TYPE_JSON = ".json";
    private static final String SEPARATOR = "_";


    public static String getNow() {
        return dateHourMinFormat.format(new Date());
    }

    public static String getSystemSeconds() {
        return secondsFormat.format(new Date());
    }

    public static String getFoldersByDate() {
        return foldersFormat.format(new Date());
    }

    public static String getNamePartCopyByDate() {
        return fileNamePartHourFormat.format(new Date());
    }

    public static String getSystemMiliSeconds() {
        return miliSecondsFormat.format(new Date());
    }

    public static String getToday() {
        return dateFormat.format(new Date());
    }

    public static String getHourFormated(Date pData) {
        return hourFormat.format(pData);
    }

    public static String getDateFormated(Date pData) {
        return dateFormat.format(pData);
    }

    public static String getCompleteDateFormated(Date pData) {
        return dateHourMinWithoutSecondFormat.format(pData);
    }

    public static String toSimpleHourFormat(String dateTime) {
        if (dateTime != null) {
            return getDateFormated(toDateFormat(dateTime));
        }

        return "";
    }

    public static String toCompleteHourFormat(String dateTime) {
        if (dateTime != null) {
            return getCompleteDateFormated(toCompleteDateFormat(dateTime));
            //return dateHourMinFormat.format(dateTime);
        }

        return "";
    }

    public static String toDataBaseDateFormat(String dateStr) {
        if (dateStr != null) {
            try {
                return dataBaseDateFormat.format(dateFormat.parse(dateStr));
            } catch (ParseException e) {
                return null;
            }
        }

        return "";
    }

    public static String toSimpleDateFormat(String dateStr) {
        if (dateStr != null && dateStr.length() > 10) {
            return dateStr.substring(0, 10);
        }

        return dateStr;
    }

    public static Date toDateFormat(String dateStr) {
        try {
            return dateStr != null && !dateStr.isEmpty() ? dateFormat.parse(dateStr) : new Date();
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date toCompleteDateFormat(String dateStr) {
        try {
            return dateStr != null && !dateStr.isEmpty() ? dateHourMinWithoutSecondFormat.parse(dateStr) : new Date();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Obtem a data de hoje sem as horas
     *
     * @return
     */
    public static Date getTodayDateOnly() {
        try {
            return dateFormat.parse(getToday());
        } catch (ParseException e) {
            return new Date();
        }
    }


    public static String getNow_EN(){
        return dataBaseDateTimeFormat.format(new Date());
    }

    public static Date extractSimpleDateFromDB(String dateStr) {
        try {
            return dateStr != null && !dateStr.isEmpty() ? dataBaseDateTimeFormat.parse(dateStr) : new Date();
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date toHourFormat(String hourStr) {
        try {
            return hourStr != null && !hourStr.isEmpty() ? hourFormat.parse(hourStr) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toCamelCase(String text) {
        if (text != null && !text.isEmpty()) {
            text = text.toLowerCase();
            String primeira = text.substring(0, 1);
            return primeira.toUpperCase() + text.substring(1);
        }

        return "";
    }

    public static boolean isHoraPosteriorOuIgual(String hora1, String hora2) {
        Date h1 = toHourFormat(hora1);
        Date h2 = toHourFormat(hora2);

        return h2 != null && h1 != null && !h2.after(h1);
    }

    public static String getFileExportFormated(Integer idObra, String dispositivo) {

        return idObra + SEPARATOR + fileNameDateFormat.format(new Date()) + SEPARATOR + dispositivo + TYPE_JSON;
    }

    public static String getFileExportFormated(String idObra, String date, String dispositivo) {
        DateFormat formatter ;
        Date d = new Date();
        SimpleDateFormat fileNameDateF = new SimpleDateFormat("ddMMyyyy");
        formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            d = formatter.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idObra + SEPARATOR + fileNameDateFormat.format(d) + SEPARATOR + dispositivo + TYPE_JSON;
    }

    public static void viewMessage(Context pContext, String pMessage, int pTitle) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(pContext);
        dialogo.setTitle(pTitle);
        dialogo.setMessage(pMessage);
        dialogo.setNeutralButton(pContext.getResources().getString(R.string.msg_ok), null);
        dialogo.show();
    }

    public static void viewMessage(Context pContext, String pMessage) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(pContext);
        dialogo.setTitle(pContext.getResources().getString(R.string.msg_aviso));
        dialogo.setMessage(pMessage);
        dialogo.setNeutralButton(pContext.getResources().getString(R.string.msg_ok), null);
        dialogo.show();
    }

    public static void viewErrorMessage(Context pContext, String pMessage) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(pContext);
        dialogo.setTitle(pContext.getResources().getString(R.string.msg_erro));
        dialogo.setMessage(pMessage);
        dialogo.setNeutralButton(pContext.getResources().getString(R.string.msg_ok), null);
        dialogo.show();
    }

    public static String[] getArrayPK(String value, String token) {

        StringTokenizer st = new StringTokenizer(value, token);

        String[] pk = new String[st.countTokens()];

        for (int i = 0; i < pk.length; i++) {

            pk[i] = st.nextToken();

        }

        return pk;

    }

    public static String gerarETicket(Context pContext, Eticket eticket, ConfiguracoesVO config) {

        String sequencia = "_XKL1RS2EF3IJ4GH5AB6CD7Q8MN9TU0VOPYWZ3IJ4Q8MNOPYWZ9TUXKL1R4GH5AB6CD";// desconsidera posição 0 p/ manter os mesmos indices das procedures (sqlserver)

        String eTicket = config.getDispositivo() + pContext.getResources().getString(R.string.TRACE);

        StringTokenizer hST = new StringTokenizer(eticket.getHoraViagem(), pContext.getResources().getString(R.string.TWO_DOTS));//Separa em hora e minutos
        StringTokenizer dST = new StringTokenizer(eticket.getDataApontamento(), pContext.getResources().getString(R.string.BAR));//Separa em  dia, mes e ano

        int hora = Integer.parseInt(hST.nextToken()) + 1;
        int minutos = Integer.parseInt(hST.nextToken()) + 1;
        int segundos = Integer.parseInt(getSystemSeconds()) + 1;

        int dia = Integer.parseInt(dST.nextToken());
        int mes = Integer.parseInt(dST.nextToken());
        int ano = Integer.parseInt(dST.nextToken()) - 2000;

        int nI = 1;

        int nCharPos = 0;

        while (nI <= 5) {

            int nIndice = ((((segundos) % 5) + nI) % 5);

            switch (nIndice) {
                case 0:
                    nCharPos = ((dia * hora * minutos) / segundos) % 60;
                    break;
                case 1:
                    nCharPos = ((mes * dia * hora) / minutos) % 60;
                    break;
                case 2:
                    nCharPos = ((ano * mes * dia) / hora) % 60;
                    break;
                case 3:
                    nCharPos = ((segundos * ano * mes) / dia) % 60;
                    break;
                case 4:
                    nCharPos = ((minutos * segundos * ano) / mes) % 60;
                    break;
            }


            if (nCharPos == 0)
                nCharPos = segundos;

            eTicket += sequencia.substring(nCharPos, nCharPos + 1);

            ++nI;
        }

        return eTicket;
    }


    public static String getString(Context pContext, String value, String url, int dir) {
        url = url.concat(pContext.getResources().getString(dir));

        url = url.replaceFirst(pContext.getResources().getString(R.string.TOKEN), value);

        return url;
    }

    public static String getString(Context pContext, String[] params, int message) {
        String msg = pContext.getResources().getString(message);

        for (int i = 0; i < params.length; i++) {
            msg = msg.replaceFirst(pContext.getResources().getString(R.string.TOKEN), params[i]);
        }

        return msg;
    }

    public static String getString(Context pContext, int param, int message) {
        String msg = pContext.getResources().getString(message);

        msg = msg.replaceFirst(pContext.getResources().getString(R.string.TOKEN), pContext.getResources().getString(param));

        return msg;
    }

    public static String getString(Context pContext, String param, int message) {

        String msg = pContext.getResources().getString(message);

        msg = msg.replaceFirst(pContext.getResources().getString(R.string.TOKEN), param);

        return msg;
    }

    public static String getMessage(Context pContext, String param, int message) {
        return getString(pContext, param, message);
    }

    public static String getMessage(Context pContext, int param, int message) {
        return getString(pContext, param, message);
    }

    public static String getMessage(Context pContext, String[] params, int message) {
        return getString(pContext, params, message);
    }

    public static String getNumVersaoApp(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return full string
        return total.toString();
    }


    /**
     * Busca a string inicial do texto informado, limitado pelo parametro limit
     *
     * @param text
     * @param limit
     * @return
     */
    public static String getInitTxt(String text, int limit) {
        if (text != null && !text.isEmpty() && text.length() > limit) {
            return text.substring(0, limit);
        }

        return text;
    }


    /**
     * Verifica se a data atual é um dia da semana
     *
     * @return
     */
    public static boolean isDiaSemana() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
    }

    /**
     * Adiciona mascara para campo data
     *
     * @param editable
     * @param e
     * @param tamanhoData
     */
    public static void addMascaraDataListener(Editable editable, EditText e, int tamanhoData) {
        final String SLASH = "/";
        String data = editable.toString();

        if (data.length() > tamanhoData) {
            if (data.length() == 2) {
                data = data + SLASH;
                e.setText(data);
            }
            if (data.length() == 3 && !data.contains(SLASH)) {
                data = data.substring(0, 2) + SLASH + data.substring(2);
                e.setText(data);
            }
            if (data.length() == 5 && data.contains(SLASH)) {
                String[] tokens = data.split(SLASH);
                data = tokens[0] + SLASH + tokens[1] + SLASH;
                e.setText(data);
            }
            if (data.length() == 6) {
                String[] tokens = data.split(SLASH);

                if (tokens.length > 0 && data.lastIndexOf(SLASH) < 5) {
                    data = tokens[0] + SLASH + data.substring(3, 5) + SLASH + data.substring(5);
                    e.setText(data);
                }
            }

            e.setSelection(data.length());
        }
    }

    public static boolean isHoraValida(String horaIni) {
        return horaIni.length() == 5 && Integer.parseInt(horaIni.substring(0, 2)) < 24 && Integer.parseInt(horaIni.substring(3)) < 60;
    }


    public static boolean isHoraValidaM(String horaIni) {
        return horaIni.length() == 5 &&
                (
                        (
                                Integer.parseInt(horaIni.substring(0, 2)) < 24
                                        &&
                                        Integer.parseInt(horaIni.substring(0, 2)) > -1
                        )
                                &&
                                (
                                        Integer.parseInt(horaIni.substring(3)) < 60
                                                &&
                                                Integer.parseInt(horaIni.substring(3)) > -1
                                )
                );
    }


    /**
     * Valida se o periodo de horas é valido, ou seja, a hora final deve ser posterior a hora inicial
     * ou a hora inicial deve ser apontada e a hora final pode nao ser informada
     *
     * @param horaIniStr
     * @param horaFimStr
     * @return
     */
    public static boolean isPeriodoHorasValido(String horaIniStr, String horaFimStr) {
        Date horaIni = toHourFormat(horaIniStr);
        Date horaFim = toHourFormat(horaFimStr);

        if (horaIni != null && horaFim != null && horaFim.after(horaIni)) {
            return true;
        }

        return horaIni != null && horaFim == null;
    }

    /**
     * Valida data no padrao dd/MM/yyyy
     *
     * @param data
     * @return
     */
    public static boolean isDataValida(String data) {
        if (data == null || data.isEmpty() || data.length() != 10) {
            return false;
        }

        Date hoje = Util.getTodayDateOnly();

        if (Util.toDateFormat(data) == null || Util.toDateFormat(data).before(hoje)) {
            return false;
        }

        int dia = Integer.parseInt(data.substring(0, 2));
        int mes = Integer.parseInt(data.substring(3, 5));
        int ano = Integer.parseInt(data.substring(6, 10));

        return dia <= 31 && mes <= 12 && ano > 2010 && ano < 2050;
    }

    /**
     * Adiciona Mascara para campo hora
     *
     * @param editable
     * @param e
     * @param componentFocus
     */
    private static void addMascaraHoraListener(Editable editable, EditText e, int tamanhoHora, EditText componentFocus) {
        final String horaTxt = editable.toString();

        //regra so é aplicada quando não estiver excluindo caracteres
        if (horaTxt.length() > tamanhoHora) {
            if (horaTxt.length() > 2 && !horaTxt.contains(COMMA)) {
                String h = horaTxt.substring(0, 2);
                String min = horaTxt.substring(2);
                e.setText(h + COMMA + min);
                e.setSelection(horaTxt.length() + 1);
            }
        }

        if (horaTxt.length() == 5) {
            componentFocus.requestFocus();
            e.clearFocus();
        }

    }

    /**
     * Adiciona mascara de hora (hh:mm)
     *
     * @param horaIniEditText
     * @param componentFocus
     */
    public static void addMascaraHora(final EditText horaIniEditText, final EditText componentFocus) {
        horaIniEditText.addTextChangedListener(new TextWatcher() {
            int tamanhoHora = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                tamanhoHora = horaIniEditText.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Util.addMascaraHoraListener(editable, horaIniEditText, tamanhoHora, componentFocus);
            }
        });
    }

    /**
     * Adiciona mascara campo decimal
     *
     * @param editable
     * @param e
     * @param numeroSize       tamanho do numero (parte inteira + parte decimal)
     * @param parteDecimalSize
     * @param tamanhoAntigo
     */
    private static void addMascaraDecimalListener(Editable editable, EditText e, int numeroSize, int parteDecimalSize, int tamanhoAntigo) {
        final String decimalTxt = editable.toString();
        int pIntSize = numeroSize - parteDecimalSize;

        //adiciona zero quando iniciar por ponto "."
        if (decimalTxt.startsWith(DOT)) {
            e.setText(ZERO + decimalTxt);
            e.setSelection(e.getText().length());
        }

        //regra so é aplicada quando não estiver excluindo caracteres
        if (decimalTxt.length() > tamanhoAntigo) {
            //adiciona o ponto automaticamente quando numeros passarem de limite de inteiros (se nao existir o ponto)
            if (decimalTxt.length() > pIntSize && !decimalTxt.contains(DOT)) {
                String pInt = decimalTxt.substring(0, pIntSize);
                String pDec = decimalTxt.substring(pIntSize);
                e.setText(pInt + DOT + pDec);
                e.setSelection(e.getText().length());
            }

            if (decimalTxt.contains(DOT)) {
                int i = decimalTxt.indexOf(DOT);
                String pInt = decimalTxt.substring(0, i);
                String pDec = decimalTxt.substring(i + 1);

                //se a parte decimal limite for extrapolada, remove a parte excedente
                if (pDec.length() > parteDecimalSize) {
                    e.setText(pInt + DOT + pDec.substring(0, parteDecimalSize));
                    e.setSelection(e.getText().length());
                }
            }
        }
    }

    /**
     * Adiciona mascara numerica a componente EditText
     *
     * @param decimalEditText
     * @param numeroSize      tamanho do numero (parte inteira + parte decimal)
     * @param decimalSize     tamanho da parte decimal
     */
    public static void addMascaraNumerica(final EditText decimalEditText, final int numeroSize, final int decimalSize) {
        decimalEditText.addTextChangedListener(new TextWatcher() {
            int tamanho = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                tamanho = decimalEditText.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Util.addMascaraDecimalListener(editable, decimalEditText, numeroSize, decimalSize, tamanho);
            }
        });
    }

    /**
     * Verifica se um intervalo de tempo (horaInicioStr e horaFimStr) existem numa List de Horas
     *
     * @param listHorarios  list de horarios
     * @param horaInicioStr
     * @param horaFimStr
     */
    public static boolean isHorarioConflitando(List<?> listHorarios, String horaInicioStr, String horaFimStr) {
        try {
            Date horaIni = toHourFormat(horaInicioStr);
            Date horaFim = toHourFormat(horaFimStr);

            for (Object o : listHorarios) {
                //Validação da HoraInicio e HoraFim do EventoVO
                if (o instanceof EventoVO) {
                    EventoVO evento = (EventoVO) o;
                    if ("Y".equalsIgnoreCase(evento.getApropriar())) {
                        if (isHorarioConflitando(horaIni, horaFim, evento.getHoraInicio(), evento.getHoraTermino())) {
                            return true;
                        }
                    }
                }

                if (o instanceof Intervalo) {
                    Intervalo intervalo = (Intervalo) o;
                    if (isHorarioConflitando(horaIni, horaFim, intervalo.getHoraIni(), intervalo.getHoraFim())) {
                        return true;
                    }
                }
                //Validação da HoraInicio e HoraFim da classe...

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * Valida se horario é valido ou se esta conflitando
     *
     * @param horaIni     hora ini sendo apontada
     * @param horaFim     hora fim sendo apontada
     * @param horaIniItem hora ini existente
     * @param horaFimItem hora fim existente
     * @return
     */
    private static boolean isHorarioConflitando(Date horaIni, Date horaFim, String horaIniItem, String horaFimItem) {
        Date h1 = toHourFormat(horaIniItem);
        Date h2 = toHourFormat(horaFimItem);

        return horaIni.after(h1) && horaIni.before(h2)
                || horaFim.after(h1) && horaFim.before(h2)
                || horaIni.equals(h1) || horaIni.equals(h2);
    }

    /**
     * Verifica se a hora esta dentro de um intervalo existente
     *
     * @param horaIni1
     * @param horaFim1
     * @param horaIni2
     * @param horaFim2
     * @return
     */
    public static boolean isInInterval(Date horaIni1, Date horaFim1, Date horaIni2, Date horaFim2) {
        if (horaFim1 != null) {
            //verifica se a horaIni2 esta no intervalo de horaIni1 e horaFim1
            if (horaIni1.before(horaIni2) && horaFim1.after(horaIni2)) {
                return true;
            }

            //verifica se a hora ini é igual
            if (!horaIni1.before(horaIni2) && !horaIni1.after(horaIni2)) {
                return true;
            }

            //verifica se a hora fim é igual
            if (horaFim2 != null && !horaFim1.before(horaFim2) && !horaFim1.after(horaFim2)) {
                return true;
            }
        }

        if (horaFim2 != null) {
            //verifica se a horaIni1 esta no intervalo de horaIni2 e horaFim2
            if (horaIni1.after(horaIni2) && horaIni1.before(horaIni2)) {
                return true;
            }

            if (horaIni1.after(horaIni2) && horaIni1.before(horaFim2)) {
                return true;
            }

            //verifica se a horaFim2 esta no intervalo de horaIni1 e horaFim1
            if (horaFim1 != null && horaIni1.before(horaFim2) && horaFim1.after(horaFim2)) {
                return true;
            }
        }

        return false;
    }


    //*************************************************QRCODE METODOS**********************************

    /**
     * Descriptografa dados criptografados na geração do QRCode
     */
    private static String descriptografar(String chave) {
        if (chave != null) {
            try {
                byte[] data = Base64.decode(chave, Base64.DEFAULT);
                //Log.i("QRCODE",chave);
                //Log.i("QRCODE",new String(data, "UTF-8"));
                return new String(data, "UTF-8");
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }


    /**
     * Método que busca os parametros do QRCode usado pelos modulos que recebem apenas o ID como parametro
     * Um TOKEN de segurança é usado para restringir procedência de dados a Constran
     *
     * @param intent
     * @return
     */
    public static Integer getQRCodeId(Context context, Intent intent) {
        QRCodeData qrCodeData = getQRCodeData(context, intent);
        return qrCodeData != null && qrCodeData.getDados() != null ? Integer.parseInt(qrCodeData.getDados()) : null;
    }

    /**
     * Método que busca os parametros do QRCode no Formulario de Motorista
     * Um TOKEN de segurança é usado para restringir procedência de dados a Constran
     *
     * @param intent
     * @return
     */
    public static ArrayList<String> getQRCodeInfos(Context context, Intent intent) {
        QRCodeData qrCodeData = getQRCodeInformations(context, intent);
        return qrCodeData != null && qrCodeData.getInfos() != null ? qrCodeData.getInfos() : null;
    }


    /**
     * Método que busca os parametros do QRCode no Formulario de Motorista
     * Um TOKEN de segurança é usado para restringir procedência de dados a Constran
     *
     * @param intent
     * @return
     */
    public static QRCodeData getQRCodeData2(Context context, Intent intent) {
        QRCodeData qrCodeData = getQRCodeInformations(context, intent);
        return qrCodeData;
    }

    /**
     * Método que busca os parametros do QRCode independetemente do tipo de parametro passado
     *
     * @param context
     * @param intent
     * @return
     */
    public static QRCodeData getQRCodeData(Context context, Intent intent) {
        String TOKEN = context.getResources().getString(R.string.TOKEN_QR_CODE);
        String SPLIT_TOKEN = context.getResources().getString(R.string.SPLIT_TOKEN_QR_CODE);
        String stringExtra = intent != null ? intent.getStringExtra(ZBarConstants.SCAN_RESULT) : null;

        if (stringExtra == null) {
            return null;
        }

        String dados = descriptografar(stringExtra);

        return new QRCodeData(TOKEN, SPLIT_TOKEN, dados);
    }

    /**
     * Método que busca os parametros do QRCode independetemente do tipo de parametro passado
     *
     * @param context
     * @param intent
     * @return
     */
    public static QRCodeData getQRCodeInformations(Context context, Intent intent) {
        ArrayList<String> infos = new ArrayList<String>();

        String SPLIT_TOKEN = context.getResources().getString(R.string.SPLIT_TOKEN_QR_CODE);
        String TOKEN = context.getResources().getString(R.string.TOKEN_QR_CODE);
        String stringExtra = intent != null ? intent.getStringExtra(ZBarConstants.SCAN_RESULT) : null;

        if(stringExtra == null) {
            return null;
        }

        String data = descriptografar(stringExtra);
        String[] params = null;

        if(data != null && !data.isEmpty() && data.contains(TOKEN)) {
            params = data.split(";");
        }

        if(params == null) {
            return null;
        } else {
            for (int i = 0; i < params.length; i++) {
                infos.add(params[i]);
            }
        }

        TipoModulo tipoModulo = TipoModulo.findByCodigo(params[1]);

        return new QRCodeData(TOKEN, tipoModulo, SPLIT_TOKEN, infos);
    }






    public static String getHoraDoDia(){
        Calendar now = Calendar.getInstance();
        Integer hora = now.get(Calendar.HOUR_OF_DAY);
        if(hora<10){
            return "0".concat(hora.toString());
        }else{
            return hora.toString();
        }
    }

    public static String getMinutoDaHora(){
        Calendar now = Calendar.getInstance();
        Integer minuto = now.get(Calendar.MINUTE);
        if(minuto<10){
            return "0".concat(minuto.toString());
        }else{
            return minuto.toString();
        }
    }

    public static boolean horaDoDiaValida(Integer horaUsr){
        Integer horaSys = Integer.valueOf(getHoraDoDia());
        if(horaUsr >= horaSys){
            return true;
        }else{
            return false;
        }
    }

    public static boolean horaDoDiaValida(String horaUsr){
        Integer horaSys = Integer.valueOf(getHoraDoDia());
        if(Integer.valueOf(horaUsr) >= horaSys){
            return true;
        }else{
            return false;
        }
    }






    //**************************************************************************************************************


    //METODOS INUTEIS

    /*
    public static String getPathLog() {
        return SD_CARD + "Constran/log/";
    }
    */


    /*
    public static String getNameFileImport(Integer idObra) {
        return idObra + TYPE_JSON;
    }
    */

    /*
    public static Object[] getPathImport(Context pContext, ConfiguracoesVO vo) throws Exception {
        String nameFile = Util.getNameFileImport(vo.getIdObra());

        String strPath = SD_CARD + pContext.getResources().getString(R.string.DIR_IMPORT);

        File path = new File(strPath);

        return new Object[]{path, nameFile};
    }
    */

    /*
    public static ImportMobile getImportVO(Context pContext, ConfiguracoesVO conf) throws Exception {

        String strPath = Util.getPathSdCard() + pContext.getResources().getString(R.string.DIR_IMPORT);

        String nameFile = Util.getNameFileImport(conf.getIdObra());

        File path = new File(strPath);

        File file = new File(path, nameFile);

        if (!file.exists()) {

            throw new AlertException(Util.getMessage(pContext, nameFile, R.string.ALERT_IMPORT_FILE_NOT_FOUND));
        }

        Reader reader = new FileReader(file);

        ImportMobile vo = GSON_API.fromJson(reader, ImportMobile.class);

        if (vo == null) {
            throw new AlertException(Util.getMessage(pContext, nameFile, R.string.ALERT_IMPORT_FILE_INVALID));
        }

        if (vo.getFrentesObra() == null || vo.getFrentesObra().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.obra, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getAtividades() == null || vo.getAtividades().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.atividade, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getEquipamentos() == null || vo.getEquipamentos().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.equipamento, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getMateriais() == null || vo.getMateriais().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.material, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getUsuarios() == null || vo.getUsuarios().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.usuario, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getServicos() == null || vo.getServicos().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.servico, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getComponentes() == null || vo.getComponentes().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.componente, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        if (vo.getParalisacoes() == null || vo.getParalisacoes().isEmpty()) {
            throw new AlertException(Util.getMessage(pContext, R.string.paralisacao, R.string.ALERT_IMPORT_SEM_DADOS));
        }

        for (ObraVO obra : vo.getObras()) {
            if (conf.getIdObra() != null && conf.getIdObra().equals(obra.getId()) || conf.getIdObra2() != null && conf.getIdObra2().equals(obra.getId())) {
                if ("S".equalsIgnoreCase(obra.getUsaOrigemDestino())) {
                    if (vo.getOrigensDestinos() == null || vo.getOrigensDestinos().isEmpty()) {
                        throw new AlertException(Util.getMessage(pContext, R.string.origemDestino, R.string.ALERT_IMPORT_SEM_DADOS));
                    }
                }
            }
        }

        return vo;
    }
    */

    /*
    public Integer getPositionArray(String pValue, String[] pArray) {
        for (int i = 0; i < pArray.length; i++) {

            if (pArray[i].equalsIgnoreCase(pValue)) {

                return i;
            }
        }
        return null;
    }
    */

    /*
    public static boolean isExistsRemoteFile(String urlPath) throws Exception {

        URL url = new URL(urlPath);
        URLConnection connection = url.openConnection();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            in.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
    */

    /*
    public static String getCardPath(Context context) {
        return getPathSdCard() + context.getResources().getString(R.string.DIR_DATABASE);
    }
    */
    /*
    public static File getFileExport(Context pContext, ConfiguracoesVO config) throws Exception {
        Object[] dados = getPathExport(pContext, config);
        return new File((File) dados[0], (String) dados[1]);
    }
    */
    /*
    public static File getFileImport(Context pContext, ConfiguracoesVO config) throws Exception {
        Object[] dados = getPathImport(pContext, config);
        return new File((File) dados[0], (String) dados[1]);
    }
    */


    /*
    public static Object[] getPathExport(Context pContext, ConfiguracoesVO vo) throws Exception {
        String nameFile = Util.getFileExportFormated(vo.getIdObra(), vo.getDispositivo());

        String strPath = getPathSdCard() + pContext.getResources().getString(R.string.DIR_EXPORT);

        File path = new File(strPath);

        return new Object[]{path, nameFile};
    }
    */


    /*
    public static Object[] getPathTemp(Context pContext) throws Exception {
        String nameFile = "temp_abs.json";

        //String strPath = getPathSdCard() + pContext.getResources().getString(R.string.DIR_TEMP);

        String strPath = AppDirectory.PATH_TEMP;

        File path = new File(strPath);

        return new Object[]{path, nameFile};
    }
    */


}

