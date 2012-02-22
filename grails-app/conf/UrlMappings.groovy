class UrlMappings {

	static excludes=['/zkau/*']

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		//"/"(view:"/index")
		//"/"(controller: "home", action:"index")
		"500"(view:'/error')

		"/"(view:"/bz/voter/management/main.zul")
	}
}
