"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[6410],{3009:(e,n,s)=>{s.r(n),s.d(n,{assets:()=>o,contentTitle:()=>l,default:()=>d,frontMatter:()=>i,metadata:()=>t,toc:()=>c});const t=JSON.parse('{"id":"rules/sequence-name","title":"sequence-name","description":"Why?","source":"@site/docs/rules/sequence-name.md","sourceDirName":"rules","slug":"/rules/sequence-name","permalink":"/liquibase-linter/docs/rules/sequence-name","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"sequence-name"},"sidebar":"docs","previous":{"title":"separate-ddl-context","permalink":"/liquibase-linter/docs/rules/separate-ddl-context"},"next":{"title":"table-name","permalink":"/liquibase-linter/docs/rules/table-name"}}');var r=s(4848),a=s(8453);const i={title:"sequence-name"},l=void 0,o={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2},{value:"Example Usage",id:"example-usage",level:2}];function u(e){const n={a:"a",code:"code",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,a.R)(),...e.components};return(0,r.jsxs)(r.Fragment,{children:[(0,r.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,r.jsxs)(n.p,{children:["You might already have a broad standard for object names - and be enforcing it with ",(0,r.jsx)(n.a,{href:"/liquibase-linter/docs/rules/object-name",children:"the object-name rule"})," - but you might also want a more specific rule concerning how sequences are named."]}),"\n",(0,r.jsx)(n.p,{children:"This rule will fail if the given regex does not match against the name when creating or renaming a sequence."}),"\n",(0,r.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,r.jsxs)(n.ul,{children:["\n",(0,r.jsxs)(n.li,{children:[(0,r.jsx)(n.code,{children:"pattern"})," - (regex, as string) regular expression that the name of any created or renamed sequence must adhere to"]}),"\n"]}),"\n",(0,r.jsx)(n.h2,{id:"example-usage",children:"Example Usage"}),"\n",(0,r.jsx)(n.pre,{children:(0,r.jsx)(n.code,{className:"language-json",children:'{\n  "rules": {\n    "sequence-name": {\n      "pattern": "^(?!seq).*$",\n      "errorMessage": "Don\'t prefix sequence names with \'seq\'"\n    }\n  }\n}\n'})}),"\n",(0,r.jsxs)(n.p,{children:["(The above example just ensures that the ",(0,r.jsx)(n.code,{children:"seq"})," prefix convention is not used.)"]})]})}function d(e={}){const{wrapper:n}={...(0,a.R)(),...e.components};return n?(0,r.jsx)(n,{...e,children:(0,r.jsx)(u,{...e})}):u(e)}},8453:(e,n,s)=>{s.d(n,{R:()=>i,x:()=>l});var t=s(6540);const r={},a=t.createContext(r);function i(e){const n=t.useContext(a);return t.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function l(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(r):e.components||r:i(e.components),t.createElement(a.Provider,{value:n},e.children)}}}]);