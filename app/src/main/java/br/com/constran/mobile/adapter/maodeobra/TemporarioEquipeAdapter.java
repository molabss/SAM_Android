package br.com.constran.mobile.adapter.maodeobra;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.AbstractAdapter;
import br.com.constran.mobile.enums.Operacao;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteTempVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PessoalVO;

import java.util.List;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class TemporarioEquipeAdapter extends AbstractAdapter<IntegranteTempVO> {

    public TemporarioEquipeAdapter(Context context, List<IntegranteTempVO> list) {
        super(context, R.layout.temporario_equipe_item, list);
    }

    @Override
    protected void preencherItensGrid(View view, int i) {
        CheckBox integranteCheckBox = (CheckBox) view.findViewById(R.id.integranteCheckBox);
        TextView integranteText = (TextView) view.findViewById(R.id.integranteText);
        TextView matriculaText = (TextView) view.findViewById(R.id.matriculaText);
        ImageView excluirImage = (ImageView) view.findViewById(R.id.excluirImage);

        IntegranteTempVO integranteEquipeVO = list.get(i);
        final PessoalVO pessoa = integranteEquipeVO.getPessoa();

        integranteText.setText(pessoa.getNome());
        matriculaText.setText(pessoa.getMatricula());

        integranteCheckBox.setVisibility(View.GONE);

        excluirImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EXCLUSAO;
                return false;
            }
        });
    }
}

