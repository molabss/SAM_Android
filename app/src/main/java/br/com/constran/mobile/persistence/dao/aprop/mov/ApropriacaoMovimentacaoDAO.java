package br.com.constran.mobile.persistence.dao.aprop.mov;

import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.aprop.mov.ApropriacaoMovimentacaoVO;
import br.com.constran.mobile.view.util.Util;

public class ApropriacaoMovimentacaoDAO extends AbstractDAO {

    private static final String APROPR_MOVIM_COL_ID_APROPRIACAO = "idApropriacao";
    private static final String APROPR_MOVIM_COL_EQUIPAMENTO = "equipamento";
    private static final String APROPR_MOVIM_COL_HORA_INICIO = "horaInicio";

    private static ApropriacaoMovimentacaoDAO instance;

    private ApropriacaoMovimentacaoDAO(Context context) {
        super(context, TBL_APROPRIACAO_MOVIMENTACAO);
    }

    public static ApropriacaoMovimentacaoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ApropriacaoMovimentacaoDAO(context);
        }

        return instance;
    }

    public void save(ApropriacaoMovimentacaoVO pVO) {

        StringBuilder builder = new StringBuilder("");

        if (pVO.getStrId() == null) {

            builder.append(" insert into ");
            builder.append(" [apropriacoesMovimentacao] ");
            builder.append(" ([apropriacao], [equipamento], [material], [origem], [destino],");
            builder.append(" [horaInicio], [dataHoraCadastro], [dataHoraAtualizacao], [horaTermino], [horimetroInicial], [horimetroFinal],");
            builder.append(" [estacaOrigemInicial], [estacaOrigemFinal], [estacaDestinoInicial], [estacaDestinoFinal],");
            builder.append(" [qtdViagens], [percentualCarga])");
            builder.append("  values ");
            builder.append(" (");
            builder.append(pVO.getIdApropriacao());
            builder.append(",");
            builder.append(pVO.getEquipamento().getId());
            builder.append(",");
            builder.append(pVO.getMaterial().getId());
            builder.append(",");
            builder.append(pVO.getOrigem().getId());
            builder.append(",");
            builder.append(pVO.getDestino().getId());
            builder.append(",");

            if (pVO.getHoraInicio() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraInicio());
                builder.append("',");
            }

            builder.append("'");
            builder.append(Util.getNow());
            builder.append("',");


            builder.append("'");
            builder.append(Util.getNow());
            builder.append("',");


            if (pVO.getHoraTermino() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraTermino());
                builder.append("',");
            }

            if (pVO.getHorimetroIni() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getHorimetroIni());
                builder.append("',");
            }

            if (pVO.getHorimetroFim() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getHorimetroFim());
                builder.append("',");
            }

            if (pVO.getEstacaOrigemInicial() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaOrigemInicial());
                builder.append("',");
            }

            if (pVO.getEstacaOrigemFinal() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaOrigemFinal());
                builder.append("',");
            }

            if (pVO.getEstacaDestinoInicial() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaDestinoInicial());
                builder.append("',");
            }

            if (pVO.getEstacaDestinoFinal() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaDestinoFinal());
                builder.append("',");
            }

            if (pVO.getQtdViagens() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getQtdViagens());
                builder.append("',");
            }

            if (pVO.getPrcCarga() == null) {
                builder.append("null");
            } else {
                builder.append("'");
                builder.append(pVO.getPrcCarga());
                builder.append("'");
            }

            builder.append(");");

        } else {

            builder.append(" update [apropriacoesMovimentacao] set ");

            builder.append(" [material]  = ");
            builder.append(pVO.getMaterial().getId());
            builder.append(" , [origem]  = ");
            builder.append(pVO.getOrigem().getId());
            builder.append(" , [destino]  = ");
            builder.append(pVO.getDestino().getId());

            builder.append(" , [horaTermino]  = ");
            if (pVO.getHoraTermino() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraTermino());
                builder.append("'");
            }

            builder.append(" , [horimetroInicial] = ");
            if (pVO.getHorimetroIni() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getHorimetroIni());
                builder.append("'");
            }

            builder.append(" , [horimetroFinal] = ");
            if (pVO.getHorimetroFim() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getHorimetroFim());
                builder.append("'");
            }

            builder.append(" , [estacaOrigemInicial] = ");
            if (pVO.getEstacaOrigemInicial() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaOrigemInicial());
                builder.append("'");
            }
            builder.append(" , [estacaOrigemFinal] = ");
            if (pVO.getEstacaOrigemFinal() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaOrigemFinal());
                builder.append("'");
            }
            builder.append(" , [estacaDestinoInicial] = ");
            if (pVO.getEstacaDestinoInicial() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaDestinoInicial());
                builder.append("'");
            }
            builder.append(" , [estacaDestinoFinal] = ");
            if (pVO.getEstacaDestinoFinal() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getEstacaDestinoFinal());
                builder.append("'");
            }

            builder.append(" , [qtdViagens] = ");
            if (pVO.getQtdViagens() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getQtdViagens());
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

            builder.append(" where [apropriacao] = ");
            builder.append(pVO.getIdApropriacao());
            builder.append(" and [equipamento]  = ");
            builder.append(pVO.getEquipamento().getId());
            builder.append(" and [horaInicio]  = ");
            builder.append("'");
            builder.append(pVO.getHoraInicio());
            builder.append("';");

        }

        execute(builder);
    }

    public String[] getPk(Object[] params) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"a.[idApropriacao], m.[equipamento], m.[horaInicio]"};

        tableJoin = " [apropriacoesMovimentacao] m ";
        tableJoin += " join [apropriacoes] a on m.[apropriacao] =  a.[idApropriacao]  ";

        conditions = "substr(a.[dataHoraApontamento],0, 11) =  '" + Util.getToday() + "'";
        conditions += " and m.[equipamento] = " + String.valueOf(params[0]);

        if (String.valueOf(params[1]).equalsIgnoreCase(getStr(R.string.ORIGEM))) {
            conditions += " and m.[origem]   =  " + String.valueOf(params[2]);
        } else if (String.valueOf(params[1]).equalsIgnoreCase(getStr(R.string.DESTINO))) {
            conditions += " and m.[destino]  =  " + String.valueOf(params[3]);
        } else if (String.valueOf(params[1]).equalsIgnoreCase(getStr(R.string.ORIGEM_DESTINO))) {
            conditions += " and m.[origem]   =  " + String.valueOf(params[2]);
            conditions += " and m.[destino]  =  " + String.valueOf(params[3]);
        }

        orderBy = " datetime(a.[dataHoraApontamento]) desc , datetime(m.[horaInicio]) desc ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] ids = null;

        if (cursor.moveToNext()) {
            ids = new String[]{cursor.getString(cursor.getColumnIndex(APROPR_MOVIM_COL_ID_APROPRIACAO)),
                    cursor.getString(cursor.getColumnIndex(APROPR_MOVIM_COL_EQUIPAMENTO)),
                    cursor.getString(cursor.getColumnIndex(APROPR_MOVIM_COL_HORA_INICIO))};
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return ids;
    }

}
