package br.com.constran.mobile.adapter.maodeobra;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.AbstractAdapter;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.AusenciaVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.PessoalVO;

import java.util.*;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ComposicaoEquipeAdapter extends AbstractAdapter<IntegranteEquipeVO> {

    private List<IntegranteEquipeVO> integrantes;
    private List<AusenciaVO> ausencias;
    private Set<IntegranteEquipeVO> checkedItems = new HashSet<IntegranteEquipeVO>();
    private Set<IntegranteEquipeVO> uncheckedItems = new HashSet<IntegranteEquipeVO>();
    private List<Boolean> itemChecked = new ArrayList<Boolean>();

    private boolean usaQRCodePessoal;

    public ComposicaoEquipeAdapter(Context context, List<IntegranteEquipeVO> integrantes, List<AusenciaVO> ausencias, boolean usaQRCodePessoal) {
        super(context, R.layout.composicao_equipe_item, integrantes);
        this.integrantes = integrantes;
        this.context = context;
        this.ausencias = ausencias;
        this.usaQRCodePessoal = usaQRCodePessoal;

        bindCheckedItems();
    }

    private void bindCheckedItems() {


        for (int i = 0; i < integrantes.size(); i++) {
            //se nao existir registros de pessoas ausentes para esta equipe, marcar todos
            if (ausencias == null || ausencias.isEmpty()) {
                itemChecked.add(i, true);
            } else {
                //caso contrario, marcar apenas os nao encontrados em ausentes

                boolean faltou = false;

                for (AusenciaVO ausencia : ausencias) {
                    Integer idIntegrante = integrantes.get(i).getPessoa().getId();

                    if (idIntegrante.equals(ausencia.getPessoa().getId())) {
                        faltou = true;
                        break;
                    }
                }

                itemChecked.add(i, !faltou);
            }
        }
    }

    public void updateItemsChecked(IntegranteVO integrante) {
        if (integrantes != null && !integrantes.isEmpty()) {
            for (int i = 0; i < integrantes.size(); i++) {
                Integer idIntegrante = integrantes.get(i).getPessoa().getId();

                if (idIntegrante.equals(integrante.getPessoa().getId())) {
                    itemChecked.set(i, true);
                    break;
                }
            }
        }
    }

    public boolean isAllItemsChecked() {
        boolean allChecked = true;

        if (itemChecked != null) {
            for (Boolean item : itemChecked) {
                if (!item) {
                    allChecked = false;
                    break;
                }
            }
        }

        return allChecked;
    }

    @Override
    protected void preencherItensGrid(View view, final int i) {
        CheckBox integranteCheckBox = (CheckBox) view.findViewById(R.id.integranteCheckBox);
        TextView integranteText = (TextView) view.findViewById(R.id.integranteText);
        TextView matriculaText = (TextView) view.findViewById(R.id.matriculaText);

        final IntegranteEquipeVO integranteEquipe = list.get(i);
        final PessoalVO pessoa = integranteEquipe.getPessoa();

        integranteText.setText(pessoa.getNome());
        matriculaText.setText(pessoa.getMatricula());

//        setIntegranteEquipeVO(integranteEquipe);

        addCheckBoxListener(i, integranteCheckBox, integranteEquipe);

        if (usaQRCodePessoal) {
            integranteCheckBox.setKeyListener(null);
        }

        integranteCheckBox.setChecked(itemChecked.get(i));
    }

    private void addCheckBoxListener(final int i, CheckBox integranteCheckBox, final IntegranteEquipeVO integranteEquipe) {
        integranteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.integranteCheckBox);
                boolean checked = cb.isChecked();

                itemChecked.set(i, checked);

                if (checked) {
                    checkedItems.add(integranteEquipe);
                    uncheckedItems.remove(integranteEquipe);
                } else {
                    checkedItems.remove(integranteEquipe);
                    uncheckedItems.add(integranteEquipe);
                }
            }
        });
    }

    public void setCheckedItems(Set<IntegranteEquipeVO> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public void setAllItemsChecked(boolean val) {
//        for(int i = 0; i<itemChecked.size(); i++) {
//            itemChecked.set(i, val);
//        }
        Collections.fill(itemChecked, val);
    }

    public void setUncheckedItems(Set<IntegranteEquipeVO> uncheckedItems) {
        this.uncheckedItems = uncheckedItems;
    }

    public Set<IntegranteEquipeVO> getCheckedItems() {
        return checkedItems;
    }

    public Set<IntegranteEquipeVO> getUncheckedItems() {
        return uncheckedItems;
    }

    public List<Boolean> getItemChecked() {
        return itemChecked;
    }

    public void setItemChecked(List<Boolean> itemChecked) {
        this.itemChecked = itemChecked;
    }
}

