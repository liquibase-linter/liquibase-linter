"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[777],{5670:(e,i,n)=>{n.r(i),n.d(i,{assets:()=>o,contentTitle:()=>r,default:()=>u,frontMatter:()=>l,metadata:()=>a,toc:()=>d});var t=n(4848),s=n(8453);const l={title:"Install"},r=void 0,a={id:"install",title:"Install",description:"Liquibase Linter is built with the Extensions feature in Liquibase, so it works by simply being on the classpath with Liquibase.",source:"@site/docs/install.md",sourceDirName:".",slug:"/install",permalink:"/liquibase-linter/docs/install",draft:!1,unlisted:!1,tags:[],version:"current",frontMatter:{title:"Install"},sidebar:"docs",next:{title:"Configure",permalink:"/liquibase-linter/docs/configure"}},o={},d=[{value:"Maven",id:"maven",level:2},{value:"Gradle",id:"gradle",level:2},{value:"Command Line",id:"command-line",level:2},{value:"Compatibility",id:"compatibility",level:2}];function c(e){const i={a:"a",code:"code",h2:"h2",li:"li",ol:"ol",p:"p",pre:"pre",...(0,s.R)(),...e.components};return(0,t.jsxs)(t.Fragment,{children:[(0,t.jsxs)(i.p,{children:["Liquibase Linter is built with ",(0,t.jsx)(i.a,{href:"https://liquibase.jira.com/wiki/spaces/CONTRIB/overview",children:"the Extensions feature in Liquibase"}),", so it works by simply being on the classpath with Liquibase."]}),"\n",(0,t.jsx)(i.h2,{id:"maven",children:"Maven"}),"\n",(0,t.jsxs)(i.ol,{children:["\n",(0,t.jsxs)(i.li,{children:["Add ",(0,t.jsx)(i.code,{children:"liquibase-linter"})," as a dependency of ",(0,t.jsx)(i.a,{href:"http://www.liquibase.org/documentation/maven/",children:"the Liquibase Maven plugin"}),":"]}),"\n",(0,t.jsxs)(i.li,{children:["Add ",(0,t.jsx)(i.code,{children:"lqlint.json"})," to the root of your project"]}),"\n"]}),"\n",(0,t.jsxs)(i.p,{children:["See this simple ",(0,t.jsx)(i.a,{href:"https://github.com/liquibase-linter/liquibase-linter/tree/main/examples/maven",children:"example"})," maven project to help get you started"]}),"\n",(0,t.jsx)(i.pre,{children:(0,t.jsx)(i.code,{className:"language-xml",children:"<plugin>\n    <groupId>org.liquibase</groupId>\n    <artifactId>liquibase-maven-plugin</artifactId>\n    <configuration>\n        ...\n    </configuration>\n    <dependencies>\n        <dependency>\n            <groupId>io.github.liquibase-linter</groupId>\n            <artifactId>liquibase-extension</artifactId>\n            <version>0.6.0</version>\n        </dependency>\n    </dependencies>\n    <executions>\n        ...\n    </executions>\n</plugin>\n"})}),"\n",(0,t.jsx)(i.h2,{id:"gradle",children:"Gradle"}),"\n",(0,t.jsxs)(i.ol,{children:["\n",(0,t.jsxs)(i.li,{children:["Add ",(0,t.jsx)(i.code,{children:"liquibase-linter"})," as a dependency of ",(0,t.jsx)(i.a,{href:"https://github.com/liquibase/liquibase-gradle-plugin",children:"the Liquibase Gradle plugin"}),":"]}),"\n",(0,t.jsxs)(i.li,{children:["Add ",(0,t.jsx)(i.code,{children:"lqlint.json"})," to the ",(0,t.jsx)(i.code,{children:"lqlint"})," directory under the root of your project"]}),"\n"]}),"\n",(0,t.jsxs)(i.p,{children:["See this simple ",(0,t.jsx)(i.a,{href:"https://github.com/liquibase-linter/liquibase-linter/tree/main/examples/gradle",children:"example"})," gradle project to help get you started"]}),"\n",(0,t.jsx)(i.pre,{children:(0,t.jsx)(i.code,{className:"language-groovy",children:"dependencies {\n    liquibaseRuntime 'org.liquibase:liquibase-core:4.29.2'\n    liquibaseRuntime 'org.liquibase:liquibase-groovy-dsl:4.0.0'\n    liquibaseRuntime 'org.hsqldb:hsqldb:2.5.0'\n    liquibaseRuntime 'io.github.liquibase-linter:liquibase-linter:0.6.0'\n    liquibaseRuntime files('lqlint')\n}\n"})}),"\n",(0,t.jsx)(i.h2,{id:"command-line",children:"Command Line"}),"\n",(0,t.jsxs)(i.ol,{children:["\n",(0,t.jsxs)(i.li,{children:["Start with the latest ",(0,t.jsx)(i.a,{href:"https://github.com/liquibase/liquibase/releases/",children:"Liquibase release zip"}),"."]}),"\n",(0,t.jsxs)(i.li,{children:["Download the latest Liquibase Linter jar from ",(0,t.jsx)(i.a,{href:"https://repo1.maven.org/maven2/io/github/liquibase-linter/",children:"maven central"})," and download\nthe ",(0,t.jsx)(i.a,{href:"https://mvnrepository.com/artifact/io.github.liquibase-linter/liquibase-linter",children:"dependencies"})," required by Liquibase Linter, then add them to\nthe ",(0,t.jsx)(i.code,{children:"lib"})," directory."]}),"\n",(0,t.jsxs)(i.li,{children:["Add your ",(0,t.jsx)(i.code,{children:"lqlint.json"})," configuration file to the ",(0,t.jsx)(i.code,{children:"lib"})," directory."]}),"\n"]}),"\n",(0,t.jsx)(i.h2,{id:"compatibility",children:"Compatibility"}),"\n",(0,t.jsx)(i.p,{children:"It doesn't matter whether you use Liquibase scripts written in XML, JSON or YAML, they will be linted just the same."}),"\n",(0,t.jsx)(i.p,{children:"Liquibase Linter has been tested with Liquibase versions 4.0 through to the latest version, so you can confidently use it with those. We'll be working to keep up with newer versions of Liquibase as they happen."}),"\n",(0,t.jsx)(i.p,{children:"As for Java support, Liquibase Linter needs at least Java 8, but you should have no issues with higher versions, unless they are with Liquibase itself."})]})}function u(e={}){const{wrapper:i}={...(0,s.R)(),...e.components};return i?(0,t.jsx)(i,{...e,children:(0,t.jsx)(c,{...e})}):c(e)}},8453:(e,i,n)=>{n.d(i,{R:()=>r,x:()=>a});var t=n(6540);const s={},l=t.createContext(s);function r(e){const i=t.useContext(l);return t.useMemo((function(){return"function"==typeof e?e(i):{...i,...e}}),[i,e])}function a(e){let i;return i=e.disableParentContext?"function"==typeof e.components?e.components(s):e.components||s:r(e.components),t.createElement(l.Provider,{value:i},e.children)}}}]);