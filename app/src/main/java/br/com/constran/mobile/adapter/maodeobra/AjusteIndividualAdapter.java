package br.com.constran.mobile.adapter.maodeobra;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.AbstractAdapter;
import br.com.constran.mobile.enums.Operacao;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PessoalVO;

import java.util.List;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class AjusteIndividualAdapter extends AbstractAdapter<IntegranteVO> {

    public AjusteIndividualAdapter(Context context, List<IntegranteVO> list) {
        super(context, R.layout.integrante_equipe_item, list);
    }

    @Override
    protected void preencherItensGrid(View view, int i) {
        setGridLineColor(view, i);

        TextView integranteText = (TextView) view.findViewById(R.id.integranteText);
        TextView matriculaText = (TextView) view.findViewById(R.id.matriculaText);
        ImageView editarImage = (ImageView) view.findViewById(R.id.editarImage);

        IntegranteVO integranteVO = list.get(i);
        final PessoalVO pessoa = integranteVO.getPessoa();

        integranteText.setText(integranteVO.toString());
        matriculaText.setText(pessoa.getMatricula());

        editarImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EDICAO;
                return false;
            }
        });

    }
}
