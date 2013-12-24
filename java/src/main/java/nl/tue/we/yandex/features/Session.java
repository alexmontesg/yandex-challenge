/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.we.yandex.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author t-jukise
 */
public class Session {
  private List<LogAction> session;
  
  public Session(){
      this.session = new ArrayList<>();
  }
  
  public void addJudge(final LogAction logAction){
      this.session.add(logAction);
  }

  public List<LogAction> getLogActions() {
      return new ArrayList<>(session);
  }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.session);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Session other = (Session) obj;
        if (!Objects.equals(this.session, other.session)) {
            return false;
        }
        return true;
    }

  
    
}
