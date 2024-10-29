"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[8180],{3805:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>o,contentTitle:()=>l,default:()=>u,frontMatter:()=>s,metadata:()=>i,toc:()=>c});var a=t(4848),r=t(8453);const s={title:"table-name"},l=void 0,i={id:"rules/table-name",title:"table-name",description:"Why?",source:"@site/docs/rules/table-name.md",sourceDirName:"rules",slug:"/rules/table-name",permalink:"/liquibase-linter/docs/rules/table-name",draft:!1,unlisted:!1,tags:[],version:"current",frontMatter:{title:"table-name"},sidebar:"docs",previous:{title:"separate-ddl-context",permalink:"/liquibase-linter/docs/rules/separate-ddl-context"},next:{title:"table-name-length",permalink:"/liquibase-linter/docs/rules/table-name-length"}},o={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2},{value:"Example Usage",id:"example-usage",level:2}];function d(e){const n={a:"a",code:"code",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,r.R)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,a.jsxs)(n.p,{children:["You might already have a broad standard for object names - and be enforcing it with ",(0,a.jsx)(n.a,{href:"/liquibase-linter/docs/rules/object-name",children:"the object-name rule"})," - but you might also want a more specific rule concerning how tables are named."]}),"\n",(0,a.jsx)(n.p,{children:"This rule will fail if the given regex does not match against the name when creating or renaming a table."}),"\n",(0,a.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,a.jsxs)(n.ul,{children:["\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.code,{children:"pattern"})," - (regex, as string) regular expression that the name of any created or renamed table must adhere to"]}),"\n"]}),"\n",(0,a.jsx)(n.h2,{id:"example-usage",children:"Example Usage"}),"\n",(0,a.jsx)(n.pre,{children:(0,a.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "table-name": {\n            "pattern": "^(?!tbl).*$",\n            "errorMessage": "Don\'t prefix table names with \'tbl\'"\n        }\n    }\n}\n'})}),"\n",(0,a.jsxs)(n.p,{children:["(The above example just ensures that the ",(0,a.jsx)(n.code,{children:"tbl"})," prefix convention is not used.)"]})]})}function u(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,a.jsx)(n,{...e,children:(0,a.jsx)(d,{...e})}):d(e)}},8453:(e,n,t)=>{t.d(n,{R:()=>l,x:()=>i});var a=t(6540);const r={},s=a.createContext(r);function l(e){const n=a.useContext(s);return a.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function i(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(r):e.components||r:l(e.components),a.createElement(s.Provider,{value:n},e.children)}}}]);