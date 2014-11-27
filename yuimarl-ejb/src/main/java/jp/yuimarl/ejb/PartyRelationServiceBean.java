/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 BitFarm Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jp.yuimarl.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.yuimarl.entity.Party;
import jp.yuimarl.entity.PartyRelation;

/**
 * Party関連操作クラス
 *
 * @author Masahiro Nitta
 */
@Stateless
public class PartyRelationServiceBean extends AbstractFacade<PartyRelation> implements PartyRelationService {

    @PersistenceContext(unitName = "yuimarlPU")
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public PartyRelationServiceBean() {
        super(PartyRelation.class);
    }

    /**
     * EntityManagerを取得する。
     *
     * @return
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Partyを指定してParty関連を取得する。
     *
     * @param p
     * @return
     */
    @Override
    public List<PartyRelation> findByParty(Party p) {
        Query q;
        List<PartyRelation> l = new ArrayList<>();
        List<PartyRelation> l1;
        List<PartyRelation> l2;
        q = em.createQuery("SELECT r FROM PartyRelation r WHERE r.party1 = :party1  ORDER BY r.relationType, r.party2.nameKana").setParameter("party1", p);
        l1 = q.getResultList();
        for (PartyRelation r : l1) {
            l.add(r);
        }
        q = em.createQuery("SELECT r FROM PartyRelation r WHERE r.party2 = :party2  ORDER BY r.relationType, r.party1.nameKana").setParameter("party2", p);
        l2 = q.getResultList();
        for (PartyRelation r : l2) {
            l.add(r);
        }
        return l;
    }

    /**
     * Partyを指定してParty関連を削除する。
     *
     * @param p
     */
    @Override
    public void deleteByParty(Party p) {
        Query q;
        q = em.createQuery("DELETE FROM PartyRelation r WHERE r.party1 = :party1").setParameter("party1", p);
        q.executeUpdate();
        q = em.createQuery("DELETE FROM PartyRelation r WHERE r.party2 = :party2").setParameter("party2", p);
        q.executeUpdate();
    }
}
