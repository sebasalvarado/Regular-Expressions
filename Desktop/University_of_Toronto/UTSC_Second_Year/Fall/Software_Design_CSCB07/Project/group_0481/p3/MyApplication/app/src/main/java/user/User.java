/** Abstract Class User.

 * @author jose, Adam, Jacob,Sebastian.
 */

package user;

import interfaces.Id;
import interfaces.Update;

public abstract class User implements Id, Update {

  String userId;

  /**
   * Sets the ID for the user.
   * @param id The ID corresponding to this user
   */
  public void setId(String id) {
    this.userId = id;
  }

  @Override
  /**
   * Retuns the ID of this user.
   * @return The ID for this user
   */
  public String getId() {
    return this.userId;
  }
}
