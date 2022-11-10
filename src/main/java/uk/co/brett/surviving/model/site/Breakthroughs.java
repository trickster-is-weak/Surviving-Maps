package uk.co.brett.surviving.model.site;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Breakthroughs")
@JsonSerialize
@JsonDeserialize
public class Breakthroughs {
    @OneToOne
    @JoinColumn(name = "site_id")
    @JsonIgnore
    Site site;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough1;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough2;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough3;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough4;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough5;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough6;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough7;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough8;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough9;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough10;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough11;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough12;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough13;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough14;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough15;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough16;

    @Enumerated(EnumType.STRING)
    Breakthrough breakthrough17;

    @Enumerated(EnumType.STRING)
    GameVariant variant;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Breakthroughs() {
    }

    public Breakthroughs(List<Breakthrough> btrs, Site site, GameVariant variant) {
        this.site = site;
        this.variant = variant;
        this.breakthrough1 = btrs.get(0);
        this.breakthrough2 = btrs.get(1);
        this.breakthrough3 = btrs.get(2);
        this.breakthrough4 = btrs.get(3);
        this.breakthrough5 = btrs.get(4);
        this.breakthrough6 = btrs.get(5);
        this.breakthrough7 = btrs.get(6);
        this.breakthrough8 = btrs.get(7);
        this.breakthrough9 = btrs.get(8);
        this.breakthrough10 = btrs.get(9);
        this.breakthrough11 = btrs.get(10);
        this.breakthrough12 = btrs.get(11);
        if (btrs.size() == 17) {
            this.breakthrough13 = btrs.get(12);
            this.breakthrough14 = btrs.get(13);
            this.breakthrough15 = btrs.get(14);
            this.breakthrough16 = btrs.get(15);
            this.breakthrough17 = btrs.get(16);
        }


    }

    public GameVariant getVariant() {
        return variant;
    }

    public void setVariant(GameVariant variant) {
        this.variant = variant;
    }

    public List<Breakthrough> getList() {
        List<Breakthrough> list = new ArrayList<>(
                List.of(breakthrough1,
                        breakthrough2,
                        breakthrough3,
                        breakthrough4,
                        breakthrough5,
                        breakthrough6,
                        breakthrough7,
                        breakthrough8,
                        breakthrough9,
                        breakthrough10,
                        breakthrough11,
                        breakthrough12));

        if (Objects.nonNull(breakthrough13)) list.add(breakthrough13);
        if (Objects.nonNull(breakthrough14)) list.add(breakthrough14);
        if (Objects.nonNull(breakthrough15)) list.add(breakthrough15);
        if (Objects.nonNull(breakthrough16)) list.add(breakthrough16);
        if (Objects.nonNull(breakthrough17)) list.add(breakthrough17);

        return list;

    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Breakthrough getBreakthrough1() {
        return breakthrough1;
    }

    public void setBreakthrough1(Breakthrough breakthrough1) {
        this.breakthrough1 = breakthrough1;
    }

    public Breakthrough getBreakthrough2() {
        return breakthrough2;
    }

    public void setBreakthrough2(Breakthrough breakthrough2) {
        this.breakthrough2 = breakthrough2;
    }

    public Breakthrough getBreakthrough3() {
        return breakthrough3;
    }

    public void setBreakthrough3(Breakthrough breakthrough3) {
        this.breakthrough3 = breakthrough3;
    }

    public Breakthrough getBreakthrough4() {
        return breakthrough4;
    }

    public void setBreakthrough4(Breakthrough breakthrough4) {
        this.breakthrough4 = breakthrough4;
    }

    public Breakthrough getBreakthrough5() {
        return breakthrough5;
    }

    public void setBreakthrough5(Breakthrough breakthrough5) {
        this.breakthrough5 = breakthrough5;
    }

    public Breakthrough getBreakthrough6() {
        return breakthrough6;
    }

    public void setBreakthrough6(Breakthrough breakthrough6) {
        this.breakthrough6 = breakthrough6;
    }

    public Breakthrough getBreakthrough7() {
        return breakthrough7;
    }

    public void setBreakthrough7(Breakthrough breakthrough7) {
        this.breakthrough7 = breakthrough7;
    }

    public Breakthrough getBreakthrough8() {
        return breakthrough8;
    }

    public void setBreakthrough8(Breakthrough breakthrough8) {
        this.breakthrough8 = breakthrough8;
    }

    public Breakthrough getBreakthrough9() {
        return breakthrough9;
    }

    public void setBreakthrough9(Breakthrough breakthrough9) {
        this.breakthrough9 = breakthrough9;
    }

    public Breakthrough getBreakthrough10() {
        return breakthrough10;
    }

    public void setBreakthrough10(Breakthrough breakthrough10) {
        this.breakthrough10 = breakthrough10;
    }

    public Breakthrough getBreakthrough11() {
        return breakthrough11;
    }

    public void setBreakthrough11(Breakthrough breakthrough11) {
        this.breakthrough11 = breakthrough11;
    }

    public Breakthrough getBreakthrough12() {
        return breakthrough12;
    }

    public void setBreakthrough12(Breakthrough breakthrough12) {
        this.breakthrough12 = breakthrough12;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
