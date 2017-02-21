package blacklist;

import static java.lang.String.format;

import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSON {

	static ScriptEngine lazyJsEngine;

	public static <T> T parseJson(String json) throws IOException, ScriptException {
		return (T) jsEngine().eval(format("Java.asJSONCompatible(%s)", json));
	}

	private static ScriptEngine jsEngine() {
		if(lazyJsEngine == null) {
			lazyJsEngine = new ScriptEngineManager().getEngineByName("javascript");
		}
		return lazyJsEngine;
	}

}
