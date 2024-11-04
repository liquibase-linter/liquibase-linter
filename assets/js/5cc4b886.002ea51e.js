"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[106],{4817:(e,n,i)=>{i.r(n),i.d(n,{assets:()=>c,contentTitle:()=>l,default:()=>u,frontMatter:()=>a,metadata:()=>t,toc:()=>d});const t=JSON.parse('{"id":"configure","title":"Configure","description":"Liquibase Linter doesn\'t have a standard configuration, so it\'s up to you to decide what you want it to enforce and how on your project.","source":"@site/docs/configure.md","sourceDirName":".","slug":"/configure","permalink":"/liquibase-linter/docs/configure","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"Configure"},"sidebar":"docs","previous":{"title":"Install","permalink":"/liquibase-linter/docs/install"},"next":{"title":"Retrofitting","permalink":"/liquibase-linter/docs/retrofitting"}}');var o=i(4848),r=i(8453),s=i(6025);const a={title:"Configure"},l=void 0,c={},d=[{value:"JSON config file",id:"json-config-file",level:2},{value:"From the classpath",id:"from-the-classpath",level:4},{value:"Custom lint config file path",id:"custom-lint-config-file-path",level:4},{value:"Reporting and <code>fail-fast</code>",id:"reporting-and-fail-fast",level:2},{value:"Ignoring certain changes",id:"ignoring-certain-changes",level:2},{value:"<code>ignore-context-pattern</code>",id:"ignore-context-pattern",level:3},{value:"<code>ignore-files-pattern</code>",id:"ignore-files-pattern",level:3},{value:"&quot;lql-ignore&quot; comments",id:"lql-ignore-comments",level:3},{value:"Importing other configuration",id:"importing-other-configuration",level:3}];function h(e){const n={a:"a",code:"code",h2:"h2",h3:"h3",h4:"h4",p:"p",pre:"pre",...(0,r.R)(),...e.components};return(0,o.jsxs)(o.Fragment,{children:[(0,o.jsx)(n.p,{children:"Liquibase Linter doesn't have a standard configuration, so it's up to you to decide what you want it to enforce and how on your project."}),"\n",(0,o.jsx)(n.h2,{id:"json-config-file",children:"JSON config file"}),"\n",(0,o.jsxs)(n.p,{children:["You do this by providing a ",(0,o.jsx)(n.code,{children:"lqlint.json"})," file at the root of your project. Here's how it's structured:"]}),"\n",(0,o.jsx)(n.pre,{children:(0,o.jsx)(n.code,{className:"language-json",children:'{\n    "fail-fast": false,\n    "ignore-context-pattern": null,\n    "ignore-files-pattern": null,\n    "rules": {}\n}\n'})}),"\n",(0,o.jsx)(n.h4,{id:"from-the-classpath",children:"From the classpath"}),"\n",(0,o.jsxs)(n.p,{children:["It is also possible for Liquibase Linter to load the ",(0,o.jsx)(n.code,{children:"lqlint.json"})," file from the classpath. This can be useful when you have many different\nprojects using Liquibase and want to share the rule config between them. With maven this would be done in the following way,\nwhere ",(0,o.jsx)(n.code,{children:"lqlint.json"})," is stored directly under ",(0,o.jsx)(n.code,{children:"src/main/resources"})," in ",(0,o.jsx)(n.code,{children:"lqlint-config"})]}),"\n",(0,o.jsx)(n.pre,{children:(0,o.jsx)(n.code,{className:"language-xml",children:"<plugin>\n    <groupId>org.liquibase</groupId>\n    <artifactId>liquibase-maven-plugin</artifactId>\n    <configuration>\n        <propertyFile>${liquibase.property.file}</propertyFile>\n    </configuration>\n    <dependencies>\n        <dependency>\n            <groupId>io.github.liquibase-linter</groupId>\n            <artifactId>liquibase-parser-extension</artifactId>\n            <version>${liquibaselinter.version}</version>\n        </dependency>\n        <dependency>\n            <groupId>com.your.group.id</groupId>\n            <artifactId>lqlint-config</artifactId>\n            <version>1.0.0-SNAPSHOT</version>\n        </dependency>\n    </dependencies>\n    ...\n</plugin>\n"})}),"\n",(0,o.jsx)(n.h4,{id:"custom-lint-config-file-path",children:"Custom lint config file path"}),"\n",(0,o.jsxs)(n.p,{children:["There is also support for changing the default path that the config file is loaded from. This is done by specifying the ",(0,o.jsx)(n.code,{children:"lqlint.config.path"}),"\nsystem property. With maven this would look like ",(0,o.jsx)(n.code,{children:"mvn resources:resources liquibase:update -Dlqlint.config.path=foo-lqlint.json"})]}),"\n",(0,o.jsxs)(n.h2,{id:"reporting-and-fail-fast",children:["Reporting and ",(0,o.jsx)(n.code,{children:"fail-fast"})]}),"\n",(0,o.jsx)(n.p,{children:"By default, lint failures are aggregated and reported at the end after all changes are scanned:"}),"\n",(0,o.jsx)("img",{alt:"Example console output for failed rules",src:(0,s.Ay)("img/console-example.png")}),"\n",(0,o.jsxs)(n.p,{children:["If you prefer, you can set ",(0,o.jsx)(n.code,{children:"fail-fast"})," to ",(0,o.jsx)(n.code,{children:"true"})," in your config file so that the process will exit as soon as it finds the first failure."]}),"\n",(0,o.jsx)(n.h2,{id:"ignoring-certain-changes",children:"Ignoring certain changes"}),"\n",(0,o.jsx)(n.h3,{id:"ignore-context-pattern",children:(0,o.jsx)(n.code,{children:"ignore-context-pattern"})}),"\n",(0,o.jsxs)(n.p,{children:["This config entry is an optional regular expression for contexts that, if found on a changeSet, will cause the linter to skip checking that changeSet. This can be useful when you have scripts in your project that were ",(0,o.jsx)(n.a,{href:"https://www.liquibase.org/documentation/generating_changelogs.html",children:"generated by a tool"})," and wouldn't pass your normal quality checks for hand-rolled scripts."]}),"\n",(0,o.jsx)(n.p,{children:"Example usage:"}),"\n",(0,o.jsx)(n.pre,{children:(0,o.jsx)(n.code,{className:"language-json",children:'{\n    "ignore-context-pattern": "^baseline.*$",\n    "rules": {}\n}\n'})}),"\n",(0,o.jsx)("small",{children:"(Regular expression literals aren't valid JSON, so you do need to use a string.)"}),"\n",(0,o.jsx)(n.h3,{id:"ignore-files-pattern",children:(0,o.jsx)(n.code,{children:"ignore-files-pattern"})}),"\n",(0,o.jsx)(n.p,{children:"This config entry is an optional regular expression for file patterns that, if matched on a changeSet, will cause the linter to skip checking that changeSet."}),"\n",(0,o.jsx)(n.p,{children:"Example usage:"}),"\n",(0,o.jsx)(n.pre,{children:(0,o.jsx)(n.code,{className:"language-json",children:'{\n    "ignore-files-pattern": "^src/main/resources/core/legacy/.*$",\n    "rules": {}\n}\n'})}),"\n",(0,o.jsxs)("small",{children:["To avoid issues with script file paths resolving to using either ",(0,o.jsx)(n.code,{children:"\\"})," or ",(0,o.jsx)(n.code,{children:"/"}),", all occurrences of ",(0,o.jsx)(n.code,{children:"\\"})," are replaced with ",(0,o.jsx)(n.code,{children:"/"})]}),"\n",(0,o.jsx)("small",{children:"(Regular expression literals aren't valid JSON, so you do need to use a string.)"}),"\n",(0,o.jsx)(n.h3,{id:"lql-ignore-comments",children:'"lql-ignore" comments'}),"\n",(0,o.jsxs)(n.p,{children:['Sometimes you might have to do something less than perfect to get you out of a jam, and it might break some of your usual quality rules. In these cases, you can include the text "lql-ignore" at the end of the changeSet\'s ',(0,o.jsx)(n.code,{children:"<comment>"})," tag and it will be skipped by the linter:"]}),"\n",(0,o.jsx)(n.pre,{children:(0,o.jsx)(n.code,{className:"language-xml",children:'<changeSet id="201809061514dg" author="dgoss">\n    <comment>Doing awful things to fix a problem lql-ignore</comment>\n    \n    \x3c!-- awful things here --\x3e\n</changeSet>\n'})}),"\n",(0,o.jsx)(n.p,{children:"You can also disable an individual rule while leaving all others on, if that's all you need:"}),"\n",(0,o.jsx)(n.pre,{children:(0,o.jsx)(n.code,{className:"language-xml",children:'<changeSet id="201809061514dg" author="dgoss">\n    <comment>Empty this whole table lql-ignore:modify-data-enforce-where</comment>\n    \n    <delete tableName="FOO"/>\n</changeSet>\n'})}),"\n",(0,o.jsx)(n.h3,{id:"importing-other-configuration",children:"Importing other configuration"}),"\n",(0,o.jsx)(n.p,{children:"The Liquibase Linter configuration can import configuration from other configuration files."}),"\n",(0,o.jsx)(n.pre,{children:(0,o.jsx)(n.code,{className:"language-json",children:'{\n    "import": [ "imported-lqlint.json" ]\n}\n'})}),"\n",(0,o.jsxs)(n.p,{children:["In this way, common configuration can be centralized. For example, common\nconfiguration could be published as a Maven artifact and included in the\nLiquibase plugin dependencies in the same way ",(0,o.jsx)(n.code,{children:"liquibase-linter"})," is included."]}),"\n",(0,o.jsx)(n.p,{children:"If multiple configurations are imported, their rules are combined. Any named\nrules in the main configuration will replace all rules of the same name from\nthe imported configuration. In this way, rules can be easily overridden."})]})}function u(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,o.jsx)(n,{...e,children:(0,o.jsx)(h,{...e})}):h(e)}},8453:(e,n,i)=>{i.d(n,{R:()=>s,x:()=>a});var t=i(6540);const o={},r=t.createContext(o);function s(e){const n=t.useContext(r);return t.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function a(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(o):e.components||o:s(e.components),t.createElement(r.Provider,{value:n},e.children)}}}]);