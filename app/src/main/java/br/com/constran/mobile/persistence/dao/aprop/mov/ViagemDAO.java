package br.com.constran.mobile.persistence.dao.aprop.mov;

import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.enums.TipoModulo;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.aprop.mov.FormMovimentacaoVO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ViagemVO;
import br.com.constran.mobile.persistence.vo.imp.OrigemDestinoVO;
import br.com.constran.mobile.view.util.Util;

public class ViagemDAO extends AbstractDAO {

    private static final String VIAGEM_COL_MATERIAL = "material";
    private static final String VIAGEM_COL_ESTACA_INI = "estacaInicial";
    private static final String VIAGEM_COL_ESTACA_FIM = "estacaFinal";
    //    private static final String VIAGEM_COL_VIRA_VIRA    = "viravira";
    private static final String VIAGEM_COL_HORIMETRO = "horimetro";
    private static final String VIAGEM_COL_HODOMETRO = "hodometro";
    private static final String VIAGEM_COL_TIPO = "tipo";
    private static final String VIAGEM_COL_EQUIP_CARGA = "equipamentoCarga";

    private static final String VIAGEM_COL_NRO_FORMULARIO = "nroFormulario";
    private static final String VIAGEM_COL_NRO_QRCODE = "nroQRCode";
    private static final String VIAGEM_COL_COD_SEGURANCA = "codSeguranca";

    private static final String VIAGEM_COL_NRO_FICHA = "nroFicha";

    private static ViagemDAO instance;

    private ViagemDAO(Context context) {
        super(context, TBL_VIAGEM_MOVIMENTACAO);
    }

    public static ViagemDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ViagemDAO(context);
        }

        return instance;
    }

    public boolean isQRCodeUsado(FormMovimentacaoVO formMovVO, String tipoLocalizacao, String dispositivo, String tipoViagem, boolean verificarDesapropriado, TipoModulo tipoModulo) {
        if(tipoModulo.equals(TipoModulo.FICHA_3_VIAS)) {
            return isQRCodeUsadoFicha3Vias(formMovVO, tipoLocalizacao, dispositivo, tipoViagem, verificarDesapropriado);
        } else if(tipoModulo.equals(TipoModulo.FICHA_MOTORISTA)) {
            return isQRCodeUsadoFichaMotorista(formMovVO, tipoLocalizacao, dispositivo, tipoViagem, verificarDesapropriado);
        } else {
            return false;
        }
    }

    public boolean isQRCodeUsadoFicha3Vias(FormMovimentacaoVO formMovVO, String tipoLocalizacao, String dispositivo, String tipoViagem, boolean verificarDesapropriado) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                " v.[apropriacao] apropriacao"
        };

        tableJoin = " [viagensMovimentacoes] v ";

        conditions = " v.[nroFicha] = '" + formMovVO.getNroFicha() + "'";

        if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {
            if (tipoViagem.equals(getStr(R.string.ORIGEM))) {
                conditions += " and v.[eticket]  = '" + dispositivo + "-" + formMovVO.geteTicketCarga() + "'";
            } else if (tipoViagem.equals(getStr(R.string.DESTINO))) {
                conditions += " and v.[eticket]  = '" + dispositivo + "-" + formMovVO.geteTicketDescarga() + "'";
            }
        } else  if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {
            conditions += " and v.[eticket]  = '" + dispositivo + "-" + formMovVO.geteTicketCarga() + "'";
        } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {
            conditions += " and v.[eticket]  = '" + dispositivo + "-" + formMovVO.geteTicketDescarga() + "'";
        }

        if(!verificarDesapropriado) {
            conditions += " and v.[apropriar]  = 'Y' ";
        }

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = new String[1];

        if (cursor.moveToNext() && dados.length > 0) {
            for (int i = 0; i < dados.length; i++) {
                dados[i] = cursor.getString(cursor.getColumnIndex("apropriacao"));
            }
        } else {
            dados = null;
        }


        if (!cursor.isClosed()) {
            cursor.close();
        }

        if(dados != null && dados.length > 0)
            return true;
        else
            return false;
    }

    public boolean isQRCodeUsadoFichaMotorista(FormMovimentacaoVO formMovVO, String tipoLocalizacao, String dispositivo, String tipoViagem, boolean verificarDesapropriado) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                " v.[apropriacao] apropriacao"
        };

        tableJoin = " [viagensMovimentacoes] v ";

        conditions = " v.[nroQRCode] = " + formMovVO.getNroQRCode();
        conditions += " and v.[nroFormulario] = " + formMovVO.getNroFormulario();

        if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {
            if (tipoViagem.equals(getStr(R.string.ORIGEM))) {
                conditions += " and v.[eticket]  = '" + dispositivo + "-" + formMovVO.geteTicketCarga() + "'";
                conditions += " and v.[codSeguranca]  = '" + formMovVO.getCodSegurancaCarga() + "'";
            } else if (tipoViagem.equals(getStr(R.string.DESTINO))) {
                conditions += " and v.[eticket]  = '" + dispositivo + "-" + formMovVO.geteTicketDescarga() + "'";
                conditions += " and v.[codSeguranca]  = '" + formMovVO.getCodSegurancaDescarga() + "'";
            }
        } else  if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.ORIGEM))) {
            conditions += " and v.[eticket]  = '" + dispositivo + "-" + formMovVO.geteTicketCarga() + "'";
            conditions += " and v.[codSeguranca]  = '" + formMovVO.getCodSegurancaCarga() + "'";
        } else if (tipoLocalizacao.equalsIgnoreCase(getStr(R.string.DESTINO))) {
            conditions += " and v.[eticket]  = '" + dispositivo + "-" + formMovVO.geteTicketDescarga() + "'";
            conditions += " and v.[codSeguranca]  = '" + formMovVO.getCodSegurancaDescarga() + "'";
        }

        if(!verificarDesapropriado) {
            conditions += " and v.[apropriar]  = 'Y' ";
        }

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = new String[1];

        if (cursor.moveToNext() && dados.length > 0) {
            for (int i = 0; i < dados.length; i++) {
                dados[i] = cursor.getString(cursor.getColumnIndex("apropriacao"));
            }
        } else {
            dados = null;
        }


        if (!cursor.isClosed()) {
            cursor.close();
        }

        if(dados != null && dados.length > 0)
            return true;
        else
            return false;
    }

    public String[] getUltimaViagem(Object[] params) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

//		columns = new String[]{"v.[material]", "v.[estacaInicial]", "v.[estacaFinal]", "v.[viravira]", "v.[tipo]", "v.[equipamentoCarga]"};
        columns = new String[]{"v.[material]", "v.[estacaInicial]", "v.[estacaFinal]", "v.[tipo]", "v.[equipamentoCarga]"};
        tableJoin = "[viagensMovimentacoes] v  ";
        tableJoin += "join[apropriacoesMovimentacao] m on v.[horaInicio] = m.[horaInicio] ";
        tableJoin += "and v.[equipamento] = m.[equipamento] and m.[apropriacao] = v.[apropriacao] ";
        tableJoin += "join [apropriacoes] a on m.[apropriacao] = a.[idApropriacao] ";
        tableJoin += "left join [equipamentos] e on v.[equipamentoCarga] = e.[idEquipamento] ";
        conditions = "substr(a.[dataHoraApontamento],0, 11) = '" + Util.getToday() + "'";
        conditions += " and a.[frentesObra] = " + String.valueOf(params[0]);
        conditions += " and a.[atividade]   = " + String.valueOf(params[1]);

        if (String.valueOf(params[2]).equalsIgnoreCase(getStr(R.string.ORIGEM))) {
            conditions += " and m.[origem]   =  " + String.valueOf(params[3]);
        } else if (String.valueOf(params[2]).equalsIgnoreCase(getStr(R.string.DESTINO))) {
            conditions += " and m.[destino]  =  " + String.valueOf(params[4]);
//		}else if(String.valueOf(params[2]).equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))){
//			conditions += " and m.[origem]   =  "+String.valueOf(params[3]);
//			conditions += " and m.[destino]  =  "+String.valueOf(params[4]);
        }

        conditions += " and datetime(substr(v.[dataHoraCadastro], 12, 8))  = (select max(datetime(substr(v2.[dataHoraCadastro], 12, 8))) from [viagensMovimentacoes] v2  where substr(v2.[dataHoraCadastro],0, 11) = '" + Util.getToday() + "')";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = null;

        if (cursor.moveToNext()) {
            dados = new String[]{cursor.getString(cursor.getColumnIndex(VIAGEM_COL_MATERIAL)),
                    cursor.getString(cursor.getColumnIndex(VIAGEM_COL_ESTACA_INI)),
                    cursor.getString(cursor.getColumnIndex(VIAGEM_COL_ESTACA_FIM)),
//                    cursor.getString(cursor.getColumnIndex(VIAGEM_COL_VIRA_VIRA)),
//                    cursor.getString(cursor.getColumnIndex(VIAGEM_COL_HORIMETRO)),
//                    cursor.getString(cursor.getColumnIndex(VIAGEM_COL_HODOMETRO)),
                    cursor.getString(cursor.getColumnIndex(VIAGEM_COL_TIPO)),
                    cursor.getString(cursor.getColumnIndex(VIAGEM_COL_EQUIP_CARGA))};
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }


    public Cursor getCursorViagensDia(Object[] params) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"v.[apropriacao]", "v.[equipamento]", "v.[horaInicio]", "v.[horaViagem]", "v.[estacaInicial] || ' ' || v.[estacaFinal]", "v.[eticket]"};

        tableJoin = "[viagensMovimentacoes] v  ";
        tableJoin += "join[apropriacoesMovimentacao] m on v.[horaInicio] = m.[horaInicio] ";
        tableJoin += "and v.[equipamento] = m.[equipamento] and m.[apropriacao] = v.[apropriacao] ";
        tableJoin += "join [apropriacoes] a on m.[apropriacao] = a.[idApropriacao] ";

        conditions = "substr(a.[dataHoraApontamento],0, 11) = '" + Util.getToday() + "'";

        conditions += " and v.[equipamento] = " + String.valueOf(params[0]);
        conditions += " and a.[frentesObra] = " + String.valueOf(params[1]);
        conditions += " and a.[atividade]   = " + String.valueOf(params[2]);

        if (String.valueOf(params[3]).equalsIgnoreCase(getStr(R.string.ORIGEM)))
            conditions += " and m.[origem]  = " + String.valueOf(params[4]);
        else
            conditions += " and m.[destino] = " + String.valueOf(params[5]);

        orderBy = "v.[horaViagem] desc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public Cursor getCursorViagensDia2(Object[] params) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"v.[apropriacao]", "v.[equipamento]", "v.[horaInicio]", "v.[horaViagem]", "v.[estacaInicial] || ' ' || v.[estacaFinal]", "v.[codSeguranca]", "v.[nroFicha]"};

        tableJoin = "[viagensMovimentacoes] v  ";
        tableJoin += "join[apropriacoesMovimentacao] m on v.[horaInicio] = m.[horaInicio] ";
        tableJoin += "and v.[equipamento] = m.[equipamento] and m.[apropriacao] = v.[apropriacao] ";
        tableJoin += "join [apropriacoes] a on m.[apropriacao] = a.[idApropriacao] ";

        conditions = "substr(a.[dataHoraApontamento],0, 11) = '" + Util.getToday() + "'";

        conditions += " and v.[equipamento] = " + String.valueOf(params[0]);
        conditions += " and a.[frentesObra] = " + String.valueOf(params[1]);
        conditions += " and a.[atividade]   = " + String.valueOf(params[2]);

        if (String.valueOf(params[3]).equalsIgnoreCase(getStr(R.string.ORIGEM)))
            conditions += " and m.[origem]  = " + String.valueOf(params[4]);
        else
            conditions += " and m.[destino] = " + String.valueOf(params[5]);

        orderBy = "v.[horaViagem] desc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }


    public String[] getValues(String[] pParams) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                " m.[idMaterial] idMaterial",
                "ifnull(e.[prefixo], '') || ' - ' || ifnull(e.[descricao], '') equipamento",
                " v.[horaViagem] horaViagem",
                " m.[descricao] material",
                " v.[estacaInicial] estacaInicial",
                " v.[estacaFinal] estacaFinal",
                " v.[eticket] editEticket",
                " v.[percentualCarga] percentual",
                " v.[horimetro] " + VIAGEM_COL_HORIMETRO,
                " v.[hodometro] " + VIAGEM_COL_HODOMETRO,
                " v.[apropriar] apropriar",
                " v.[observacoes] observacoes",
                " v.[peso] peso",
                " v.[tipo] tipo",
                " v.[equipamentoCarga] equipamentoCarga",
                " v.[nroFormulario] nroFormulario",
                " v.[nroQRCode] nroQRCode",
                " v.[codSeguranca] codSeguranca",
                " v.[nroFicha] nroFicha"
        };

        tableJoin = " [viagensMovimentacoes] v ";
        tableJoin += " join [apropriacoesMovimentacao] am on v.[horaInicio] = am.[horaInicio]  ";
        tableJoin += "   and v.[equipamento] = am.[equipamento]  ";
        tableJoin += "   and v.[apropriacao] = am.[apropriacao]  ";
        tableJoin += " left join [materiais] m  on m.[idMaterial] = v.[material]  ";
        tableJoin += " join [equipamentos] e  on e.[idEquipamento] = v.[equipamento]  ";

        conditions = " v.[apropriacao] = " + pParams[0];
        conditions += " and v.[equipamento] = " + pParams[1];
        conditions += " and v.[horaInicio]  = '" + pParams[2] + "'";
        conditions += " and v.[horaViagem]  = '" + pParams[3] + "'";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = preencherDados(cursor);

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public Cursor getCursorSearch(Object[] params) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"e.[prefixo]", "v.[estacaInicial]", "v.[horaViagem]", "ifnull(v.[eticket],' - ')", "m.[descricao]",
                " case when v.[apropriar] = 'Y' then 'Sim' when v.[apropriar] = 'N' then '" + getStr(R.string.NAO) + "' END as apropriar"};

        tableJoin = "[viagensMovimentacoes] v  ";
        tableJoin += "join[apropriacoesMovimentacao] m on v.[horaInicio] = m.[horaInicio] ";
        tableJoin += "and v.[equipamento] = m.[equipamento] and m.[apropriacao] = v.[apropriacao] ";
        tableJoin += "join[equipamentos] e on e.[idEquipamento] = v.[equipamento] ";
        tableJoin += "join[materiais] m on m.[idMaterial] = v.[material] ";
        tableJoin += "join [apropriacoes] a on m.[apropriacao] = a.[idApropriacao] ";
        conditions = "substr(a.[dataHoraApontamento],0, 11) = '" + String.valueOf(params[0]) + "'";

        //filtro equipamento
        if (params[1] != null) {
            conditions += " and v.[equipamento] = " + Integer.valueOf(String.valueOf(params[1]));
        }

        //filtro material
        if (params.length >= 2 && params[2] != null) {
            conditions += " and m.[idMaterial] = " + Integer.valueOf(String.valueOf(params[2]));
        }

        //filtro estaca
        if (params.length >= 3 && params[3] != null) {
            conditions += " and v.[estacaInicial] = '" + String.valueOf(params[3]) + "'";
        }

        orderBy = "e.[prefixo] asc, v.[estacaInicial] asc, v.[horaViagem] asc ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }


    public void save(ViagemVO pVO) {

        StringBuilder builder = new StringBuilder("");

        if (pVO.getStrId() == null) {

            builder.append(" insert into ");
            builder.append(" [viagensMovimentacoes] ");
            builder.append(" ([apropriacao], [equipamento], [material], ");
            builder.append(" [horaInicio], [horaViagem], [estacaInicial], [estacaFinal],");
            builder.append(" [eticket], [percentualCarga], [tipo],");
			builder.append(" [codSeguranca], [nroFicha], [nroQRCode], [nroFormulario], ");
            builder.append( VIAGEM_COL_HORIMETRO + " , " + VIAGEM_COL_HODOMETRO + " , ");
            builder.append(" [apropriar], [observacoes], [dataHoraCadastro], [dataHoraAtualizacao], [peso], [equipamentoCarga])");
            builder.append("  values ");
            builder.append(" (");
            builder.append(pVO.getIdApropriacao());
            builder.append(",");
            builder.append(pVO.getIdEquipamento());
            builder.append(",");
            builder.append(pVO.getMaterial().getId());
            builder.append(",");

            if (pVO.getHoraInicio() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraInicio());
                builder.append("',");
            }

            if (pVO.getHoraViagem() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraViagem());
                builder.append("',");
            }

            if (pVO.getEstacaIni() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaIni());
                builder.append("',");
            }

            if (pVO.getEstacaFim() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaFim());
                builder.append("',");
            }

            if (pVO.getEticket() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getEticket());
                builder.append("',");
            }

            if (pVO.getPrcCarga() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getPrcCarga());
                builder.append("',");
            }

            if (pVO.getTipo() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getTipo());
                builder.append("',");
            }

            if(pVO.getCodSeguranca() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getCodSeguranca());
                builder.append("',");
            }

            if(pVO.getNroFicha() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getNroFicha());
                builder.append("',");
            }

            if(pVO.getNroQRCode() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getNroQRCode());
                builder.append("',");
            }

            if(pVO.getNroFormulario() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getNroFormulario());
                builder.append("',");
            }

            if (pVO.getHorimetro() == null) {
                builder.append("'N',");
            } else {
                builder.append("'");
                builder.append(pVO.getHorimetro());
                builder.append("',");
            }

            if (pVO.getHodometro() == null) {
                builder.append("'N',");
            } else {
                builder.append("'");
                builder.append(pVO.getHodometro());
                builder.append("',");
            }

            if (pVO.getApropriar() == null) {
                builder.append("'N',");
            } else {
                builder.append("'");
                builder.append(pVO.getApropriar());
                builder.append("',");
            }

            if (pVO.getObservacoes() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getObservacoes());
                builder.append("',");
            }

            builder.append("'");
            builder.append(Util.getNow());
            builder.append("',");

            builder.append("'");
            builder.append(Util.getNow());
            builder.append("',");

            if (pVO.getPeso() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getPeso());
                builder.append("',");
            }

            if (pVO.getIdEquipamentoCarga() == null) {
                builder.append("null");
            } else {
                builder.append(pVO.getIdEquipamentoCarga());
            }
            builder.append(");");

        } else {

            builder.append(" update [viagensMovimentacoes] set ");

            builder.append(" [equipamentoCarga]  = ");
            builder.append(pVO.getIdEquipamentoCarga());

            builder.append(", [material]  = ");
            builder.append(pVO.getMaterial().getId());

            builder.append(" , [horaViagem]  = ");

            if (pVO.getHoraViagem() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraViagem());
                builder.append("'");
            }

            builder.append(" , [estacaInicial] = ");
            if (pVO.getEstacaIni() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaIni());
                builder.append("'");
            }

            builder.append(" , [estacaFinal] = ");
            if (pVO.getEstacaFim() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaFim());
                builder.append("'");
            }

            builder.append(" , [eticket] = ");
            if (pVO.getEticket() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getEticket());
                builder.append("'");
            }

            builder.append(" , [percentualCarga] = ");
            if (pVO.getPrcCarga() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getPrcCarga());
                builder.append("'");
            }

            builder.append(" , [codSeguranca] = ");
            if(pVO.getCodSeguranca() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getCodSeguranca());
                builder.append("'");
            }

            builder.append(" , [nroFicha] = ");
            if(pVO.getNroFicha() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getNroFicha());
                builder.append("'");
            }

            builder.append(" , [nroQRCode] = ");
            if(pVO.getNroQRCode() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getNroQRCode());
                builder.append("'");
            }

            builder.append(" , [nroFormulario] = ");
            if(pVO.getNroFormulario() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getNroFormulario());
                builder.append("'");
            }

            builder.append(" , " + VIAGEM_COL_HORIMETRO + " = ");
            builder.append("'");
            builder.append(pVO.getHorimetro() != null ? pVO.getHorimetro() : 'N');
            builder.append("'");

            builder.append(" , " + VIAGEM_COL_HODOMETRO + " = ");
            builder.append("'");
            builder.append(pVO.getHorimetro() != null ? pVO.getHodometro() : 'N');
            builder.append("'");

            builder.append(" , [apropriar]  = ");
            builder.append("'");
            builder.append(pVO.getApropriar());
            builder.append("'");
            builder.append(" , [observacoes]  = ");
            builder.append("'");
            builder.append(pVO.getObservacoes());
            builder.append("'");
            builder.append(" , [dataHoraAtualizacao]  = ");
            builder.append("'");
            builder.append(Util.getNow());
            builder.append("'");


            builder.append(" , [peso] = ");
            if (pVO.getPeso() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getPeso());
                builder.append("'");
            }

            builder.append(" where [apropriacao] = ");
            builder.append(pVO.getIdApropriacao());
            builder.append(" and [equipamento]  = ");
            builder.append(pVO.getIdEquipamento());
            builder.append(" and [horaInicio]  = ");
            builder.append("'");
            builder.append(pVO.getHoraInicio());
            builder.append("'");
            builder.append(" and [horaViagem]  = ");
            builder.append("'");
            builder.append(pVO.getHoraViagem());
            builder.append("';");

        }

        execute(builder);
    }


    public String[] getArrayDatas() {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"substr(a.[dataHoraApontamento],0, 11)"};

        tableJoin = " [viagensMovimentacoes] v ";
        tableJoin += " join [apropriacoes] a on a.[idApropriacao] = v.[apropriacao]  ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = new String[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = cursor.getString(0);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;

    }

    @Override
    protected String[] getColunas() {
        return new String[]{
                ALIAS_ID_MATERIAL,
                ALIAS_EQUIPAMENTO,
                ALIAS_HORA_VIAGEM,
                ALIAS_MATERIAL,
                ALIAS_ESTACA_INI,
                ALIAS_ESTACA_FIM,
                ALIAS_EDIT_ETICKET,
                ALIAS_PERC,
//                ALIAS_VIRA_VIRA,
                VIAGEM_COL_HORIMETRO,
                VIAGEM_COL_HODOMETRO,
                ALIAS_APROPRIAR,
                ALIAS_OBS,
                ALIAS_PESO,
                ALIAS_TIPO,
                ALIAS_EQUIP_CARGA,
                VIAGEM_COL_NRO_FORMULARIO,
                VIAGEM_COL_NRO_QRCODE,
                VIAGEM_COL_COD_SEGURANCA,
                VIAGEM_COL_NRO_FICHA
        };
    }
}
