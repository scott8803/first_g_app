package first_g_app

class Micropost {
		String content
		
		static belongsTo = User
		
    static constraints = {
    	content (maxSize: 25)
    }
}
