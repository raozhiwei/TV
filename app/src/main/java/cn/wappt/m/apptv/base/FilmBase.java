package cn.wappt.m.apptv.base;



/**
 * @author: wsq
 * @date: 2020/11/4
 * Description:
 */
public class FilmBase implements Comparable<FilmBase> {
    int ids;
    String film_name;
    String film_image;
    String film_url;
    String film_remarks;


    public String getFilm_remarks() {
        return film_remarks;
    }

    public void setFilm_remarks(String film_remarks) {
        this.film_remarks = film_remarks;
    }

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getFilm_name() {
        return film_name;
    }

    public void setFilm_name(String film_name) {
        this.film_name = film_name;
    }

    public String getFilm_image() {
        return film_image;
    }

    public void setFilm_image(String film_image) {
        this.film_image = film_image;
    }

    public String getFilm_url() {
        return film_url;
    }

    public void setFilm_url(String film_url) {
        this.film_url = film_url;
    }

    public FilmBase(int ids, String film_name, String film_image, String film_url) {
        this.ids = ids;
        this.film_name = film_name;
        this.film_image = film_image;
        this.film_url = film_url;
    }

    public FilmBase() {
    }


    @Override
    public int compareTo(FilmBase o) {
        return 0;
    }
}
