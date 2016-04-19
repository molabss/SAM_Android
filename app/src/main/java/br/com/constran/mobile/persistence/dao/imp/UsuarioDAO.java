package br.com.constran.mobile.persistence.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;

public class UsuarioDAO extends AbstractDAO {

    private static final String USUARIO_COL_COD_USUARIO = "codUsuario";
    private static final String USUARIO_COL_SENHA = "senha";
    private static final String USUARIO_COL_GRUPO = "grupo";
    private static final String USUARIO_COL_FUNCAO = "funcao";
    private static final String USUARIO_COL_ID_USUARIO = "idUsuario";
    private static final String USUARIO_COL_ID_USUARIO_PES = "idUsuarioPessoal";
    private static final String USUARIO_COL_NOME = "nome";
    private static final String USUARIO_COL_QRCODE = "qrcode";
    private static final String USUARIO_COL_CC_OBRA = "ccobra";

    private static UsuarioDAO instance;

    private UsuarioDAO(Context context) {
        super(context, TBL_USUARIO);
    }

    public static UsuarioDAO getInstance(Context context) {
        if (instance == null) {
            instance = new UsuarioDAO(context);
        }

        return instance;
    }

    public UsuarioVO[] getArrayUsuarioVOByObra(String idObra, String idObra2) {

        Query query = new Query(true);

        query.setColumns(new String[]
                        {
                                USUARIO_COL_COD_USUARIO,
                                "ifnull([idUsuario],[idUsuarioPessoal]) " + ALIAS_ID_USUARIO_PES,
                                "' ' || [nome] " + ALIAS_NOME,
                                USUARIO_COL_SENHA,
                                "ifnull([idUsuario],'P') " + ALIAS_ID_USUARIO,
                                USUARIO_COL_GRUPO,
                                USUARIO_COL_FUNCAO
                        }
        );

        query.setTableJoin(TBL_USUARIO);

        if (idObra2.isEmpty()) {
            query.setConditions("[ccobra] = ? and [grupo] = 'apontadorObra' ");
            //query.setConditions("[ccobra] = ? ");
            query.setConditionsArgs(new String[]{idObra});
        } else {
            query.setConditions("[ccobra] = ? or [ccobra] = ? and [grupo] = 'apontadorObra' ");
            //query.setConditions("[ccobra] = ? or [ccobra] = ? ");

            query.setConditionsArgs(new String[]{idObra, idObra2});
        }

        query.setOrderBy("[nome] ASC");

        Cursor cursor = getCursor(query);

        UsuarioVO[] dados = new UsuarioVO[cursor.getCount() + 1];

        int i = 0;

        dados[i++] = new UsuarioVO(0, 0, getStr(R.string.NAO_INFORMADO), "", "", "", "");

        while (cursor.moveToNext()) {
            dados[i++] = new UsuarioVO(
                    cursor.getInt(cursor.getColumnIndex(USUARIO_COL_COD_USUARIO)),
                    cursor.getInt(cursor.getColumnIndex(ALIAS_ID_USUARIO_PES)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_NOME)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_SENHA)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_ID_USUARIO)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_GRUPO)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_FUNCAO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public UsuarioVO[] getArrayUsuarioVO() {

        Query query = new Query(true);

        query.setColumns(new String[]
                        {
                                USUARIO_COL_COD_USUARIO,
                                "ifnull([idUsuario],[idUsuarioPessoal]) " + ALIAS_ID_USUARIO_PES,
                                "' ' || [nome] " + ALIAS_NOME,
                                USUARIO_COL_SENHA,
                                "ifnull([idUsuario],'P') " + ALIAS_ID_USUARIO,
                                USUARIO_COL_GRUPO,
                                USUARIO_COL_FUNCAO
                        }
        );

        query.setTableJoin(TBL_USUARIO);

        query.setOrderBy("[nome] ASC");

        Cursor cursor = getCursor(query);

        UsuarioVO[] dados = new UsuarioVO[cursor.getCount() + 1];

        int i = 0;

        dados[i++] = new UsuarioVO(0, 0, getStr(R.string.NAO_INFORMADO), "", "", "", "");

        while (cursor.moveToNext()) {
            dados[i++] = new UsuarioVO(
                    cursor.getInt(cursor.getColumnIndex(USUARIO_COL_COD_USUARIO)),
                    cursor.getInt(cursor.getColumnIndex(ALIAS_ID_USUARIO_PES)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_NOME)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_SENHA)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_ID_USUARIO)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_GRUPO)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_FUNCAO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public UsuarioVO[] getArrayUsuarioAbastecedor() {
        return getArrayUsuarioByFuncao("%ABASTECEDOR%");
    }

    public UsuarioVO[] getArrayUsuarioOperador() {
        return getArrayUsuarioByFuncao("%OPERADOR%");
    }

    public UsuarioVO[] getArrayUsuarioByFuncao(String funcao) {
        Query query = new Query(true);

        query.setColumns(new String[]{USUARIO_COL_COD_USUARIO, USUARIO_COL_ID_USUARIO_PES,
                "' ' || [nome] " + ALIAS_NOME, USUARIO_COL_SENHA,
                "ifnull([idUsuario],'P') " + ALIAS_ID_USUARIO, USUARIO_COL_GRUPO, USUARIO_COL_FUNCAO});

        query.setTableJoin(TBL_USUARIO);

        query.setConditions("upper([funcao]) like '" + funcao + "' ");

        query.setOrderBy("[nome] ASC");

        Cursor cursor = getCursor(query);

        UsuarioVO[] dados = new UsuarioVO[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = new UsuarioVO(
                    cursor.getInt(cursor.getColumnIndex(USUARIO_COL_COD_USUARIO)),
                    cursor.getInt(cursor.getColumnIndex(USUARIO_COL_ID_USUARIO_PES)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_NOME)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_SENHA)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_ID_USUARIO)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_GRUPO)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_FUNCAO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }

    public UsuarioVO getById(Integer codUsuario) {
        Query query = new Query(true);

        query.setColumns(new String[]{USUARIO_COL_COD_USUARIO,
                USUARIO_COL_ID_USUARIO_PES, USUARIO_COL_NOME, USUARIO_COL_SENHA,
                "ifnull([idUsuario],'P') " + ALIAS_ID_USUARIO, USUARIO_COL_GRUPO, USUARIO_COL_FUNCAO});

        query.setTableJoin(TBL_USUARIO);

        query.setConditions("[codUsuario] = ? ");

        query.setConditionsArgs(new String[]{codUsuario.toString()});

        Cursor cursor = getCursor(query);

        UsuarioVO user = null;

        if (cursor.moveToNext()) {
            user = new UsuarioVO(
                    cursor.getInt(cursor.getColumnIndex(USUARIO_COL_COD_USUARIO)),
                    cursor.getInt(cursor.getColumnIndex(USUARIO_COL_ID_USUARIO_PES)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_NOME)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_SENHA)),
                    cursor.getString(cursor.getColumnIndex(ALIAS_ID_USUARIO)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_GRUPO)),
                    cursor.getString(cursor.getColumnIndex(USUARIO_COL_FUNCAO)));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return user;
    }

    public Integer getIdPessoalByQRCode(String qrcode) {
        Query query = new Query(true);

        query.setColumns(new String[]{USUARIO_COL_ID_USUARIO_PES});

        query.setTableJoin(TBL_USUARIO);

        query.setConditions("[qrcode] = ? ");

        query.setConditionsArgs(new String[]{qrcode});

        Cursor cursor = getCursor(query);

        Integer idPessoal = 0;

        if (cursor.moveToNext()) {
            idPessoal = cursor.getInt(cursor.getColumnIndex(USUARIO_COL_ID_USUARIO_PES));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return idPessoal;
    }


    public void save(UsuarioVO pVO) {
        insert(getContentValues(pVO));
    }

    @Override
    protected ContentValues getContentValues(Object abstractVO) {
        UsuarioVO pVO = (UsuarioVO) abstractVO;

        ContentValues contentValues = new ContentValues();
        contentValues.put(USUARIO_COL_ID_USUARIO, pVO.getIdUsuario());
        contentValues.put(USUARIO_COL_ID_USUARIO_PES, pVO.getIdUsuarioPessoal());
        contentValues.put(USUARIO_COL_NOME, pVO.getNome());
        contentValues.put(USUARIO_COL_SENHA, pVO.getSenha());
        contentValues.put(USUARIO_COL_GRUPO, pVO.getGrupo());
        contentValues.put(USUARIO_COL_FUNCAO, pVO.getFuncao());
        contentValues.put(USUARIO_COL_QRCODE, pVO.getQrcode());
        contentValues.put(USUARIO_COL_CC_OBRA, pVO.getCcObra());

        return contentValues;
    }

}

