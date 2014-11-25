package mnm.mods.tabbychat.core.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.listener.TabbyAddon;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.LogHelper;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

public class TabbyEnumerator {

    private static LogHelper logger = TabbyChat.getLogger();

    public static void detectAddonsOnClasspath(LaunchClassLoader classLoader) {
        List<Class<? extends TabbyAddon>> list = Lists.newArrayList();
        URL[] urls = classLoader.getURLs();

        for (URL url : urls) {
            try {
                list.addAll(findMods(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Class<? extends TabbyAddon> base : list) {
            try {
                registerAddon(base);
            } catch (Exception e) {
                logger.error("Unable to instantiate " + base.getName());
                e.printStackTrace();
            }
        }
    }

    private static void registerAddon(Class<? extends TabbyAddon> module)
            throws InstantiationException, IllegalAccessException {
        TabbyAddon chatBase = module.newInstance();
        TabbyProvider.getInstance().addBaseListener(chatBase);
        logger.info("Successfully added module " + chatBase.getClass().getSimpleName() + '.');
    }

    @SuppressWarnings("unchecked")
    private static List<Class<? extends TabbyAddon>> findMods(URL url) throws IOException {
        List<Class<? extends TabbyAddon>> list = Lists.newArrayList();

        List<String> classesToSearch = Lists.newArrayList();
        try {
            File file = new File(url.toURI());
            if (file.isDirectory()) {
                // We're in a dev environment.
                classesToSearch.addAll(listClassNames(file));
            } else if (file.isFile()) { // This might not exist.
                JarFile jar = new JarFile(file);
                classesToSearch.addAll(listJarEntries(jar.entries()));
                jar.close();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        for (String module : classesToSearch) {
            String end;
            if (module.contains("."))
                end = module.substring(module.lastIndexOf('.') + 1);
            else
                end = module;
            if (end.startsWith(TabbyRef.API_PREFIX)) {
                try {
                    Class<?> clazz = Class.forName(module);
                    if (TabbyAddon.class.isAssignableFrom(clazz)) {
                        list.add((Class<? extends TabbyAddon>) clazz);
                        logger.info("Adding " + clazz.getName() + " to list of modules.");
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /*
     * Gets a list of class names in the directory.
     *
     * @param parentDir The directory representing the default package.
     *
     * @return A list of class names
     */
    private static List<String> listClassNames(File parentDir) {
        List<String> list = Lists.newArrayList();
        for (File file : FileUtils.listFiles(parentDir, new String[] { "class" }, true))
            list.add(getRelativePath(parentDir, file).replace(".class", "").replace(
                    File.separatorChar, '.'));
        return list;
    }

    private static String getRelativePath(File parent, File child) {
        // remove leading slash
        return child.getPath().replace(parent.getPath(), "").substring(1);
    }

    /*
     * Converts a JarEntry Enumeration to a list of class names.
     */
    private static List<String> listJarEntries(Enumeration<JarEntry> entries) {
        List<String> list = Lists.newArrayList();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.endsWith(".class"))
                list.add(name.replace(".class", "").replace('/', '.'));
        }
        return list;
    }
}