package first_g_app

class User {
		String name
		String email
		
		static hasMany = [users: User]
		
    static constraints = {
    	name()
    	email(blank:true)
    }
}
